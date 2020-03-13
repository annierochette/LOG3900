using PolyPaint.Utilitaires;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Input;
using System.Windows.Input.StylusPlugIns;
using System.Windows.Media;
using System.Windows.Shapes;

namespace PolyPaint.Modeles
{
    public class CustomDynamicRenderer: System.Windows.Input.StylusPlugIns.DynamicRenderer
    {

        private static AppSocket socket = AppSocket.Instance;
        private string path;
        

        public CustomDynamicRenderer(): base()
        {

        }

        protected override void OnStylusDown(RawStylusInput rawStylusInput)
        {
            base.OnStylusDown(rawStylusInput);
        }

        protected override void OnStylusUp(RawStylusInput rawStylusInput)
        {
            base.OnStylusUp(rawStylusInput);
        }

        protected override void OnDraw(DrawingContext drawingContext,
                                   StylusPointCollection stylusPoints,
                                   Geometry geometry, Brush fillBrush)
        {
            socket.Emit("draw", "General", geometry.ToString());            
            base.OnDraw(drawingContext, stylusPoints, geometry,fillBrush);
        }

        private void appendGeometry(string toAppend)
        {
            path = path.Remove(path.Length - 1);
            path += toAppend.Remove(0, 3);
        }
    }
}
