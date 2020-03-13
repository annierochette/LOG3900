using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using System.Windows.Input.StylusPlugIns;

namespace PolyPaint.Modeles
{
    class DraftsmanPlugIn : StylusPlugIn
    {
        // EventArgs for the StrokeRendered event.
        public class StrokeRenderedEventArgs : EventArgs
        {
            public StrokeRenderedEventArgs(StylusPointCollection points)
            {
                StrokePoints = points;
            }

            public StylusPointCollection StrokePoints { get; }
        }

        // EventHandler for the StrokeRendered event.
        public delegate void StrokeRenderedEventHandler(object sender, StrokeRenderedEventArgs e);
        
        StylusPointCollection collectedPoints;
        public event StrokeRenderedEventHandler StrokeRendered;

        protected override void OnStylusMove(RawStylusInput rawStylusInput)
        {
            // Run the base class before modifying the data
            base.OnStylusMove(rawStylusInput);

            if (rawStylusInput.GetStylusPoints().Count >= 25)
            {
                rawStylusInput.NotifyWhenProcessed(rawStylusInput.GetStylusPoints());
            }
        }

        protected override void OnStylusUp(RawStylusInput rawStylusInput)
        {
            // Run the base class before modifying the data
            base.OnStylusUp(rawStylusInput);

            StylusPointCollection pointsFromEvent = rawStylusInput.GetStylusPoints();

                // Restrict the stylus input and add the filtered 
                // points to collectedPoints. 
             

                // Subscribe to the OnStylusUpProcessed method.
                rawStylusInput.NotifyWhenProcessed(collectedPoints);

        }

        private StylusPointCollection FilterPackets(StylusPointCollection stylusPoints)
        {
            // Modify the (X,Y) data to move the points 
            // inside the acceptable input area, if necessary
            for (int i = 0; i < stylusPoints.Count; i++)
            {
                StylusPoint sp = stylusPoints[i];
                if (sp.X < 50) sp.X = 50;
                if (sp.X > 250) sp.X = 250;
                if (sp.Y < 50) sp.Y = 50;
                if (sp.Y > 250) sp.Y = 250;
                stylusPoints[i] = sp;
            }

            // Return the modified StylusPoints.
            return stylusPoints;
        }

        // This is called on the application thread.  
        protected override void OnStylusUpProcessed(object callbackData, bool targetVerified)
        {
            // Check that the element actually receive the OnStylusUp input.
            if (targetVerified)
            {
                StylusPointCollection strokePoints = callbackData as StylusPointCollection;

                if (strokePoints == null)
                {
                    return;
                }

                // Raise the StrokeRendered event so the consumer of the plugin can
                // add the filtered stroke to its StrokeCollection.
                StrokeRenderedEventArgs e = new StrokeRenderedEventArgs(strokePoints);
                OnStrokeRendered(e);
            }
        }

        protected virtual void OnStrokeRendered(StrokeRenderedEventArgs e)
        {
            if (StrokeRendered != null)
            {
                StrokeRendered(this, e);
            }
        }
    }
}
