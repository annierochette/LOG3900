using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Ink;
using System.Windows.Input;

namespace PolyPaint.Modeles
{
    class Game
    {
        const int X = 0;
        const int Y = 1;
        [JsonProperty]
        public string word { get; set; }

        [JsonProperty]
        public double[][][] drawing { get; set; }

        private StrokeCollection Strokes()
        {
            StrokeCollection strokes = new StrokeCollection();
            for (int stroke = 0; stroke < drawing.Length; stroke++)
            {
                StylusPointCollection spc = new StylusPointCollection();
                for (int point = 0; point < drawing[stroke][X].Length; point++)
                {
                    spc.Add(new StylusPoint(drawing[stroke][X][point], drawing[stroke][Y][point]));
                }

                strokes.Add(new Stroke(spc));
            }

            return strokes;    
        }

        public StrokeCollection CollapsedStrokes()
        {
            return ResizedStrokes(1, 1);
        }

        public StrokeCollection ResizedStrokes(double actualWidth, double actualHeigth)
        {
            const double resizeFactor = 0.75;
            StrokeCollection strokes = Strokes();
            double width = strokes.Max(stroke => stroke.StylusPoints.Max(point => point.X));
            double heigth = strokes.Max(stroke => stroke.StylusPoints.Max(point => point.Y));

            StrokeCollection collapsedStrokes = new StrokeCollection();
            foreach (Stroke stroke in strokes)
            {
                StylusPointCollection spc = new StylusPointCollection();

                foreach (StylusPoint point in stroke.StylusPoints)
                {
                    spc.Add(new StylusPoint(point.X / width * actualWidth * resizeFactor, point.Y / heigth * actualHeigth * resizeFactor));
                }

                collapsedStrokes.Add(new Stroke(spc));
            }

            return collapsedStrokes;
        }

    }
}
