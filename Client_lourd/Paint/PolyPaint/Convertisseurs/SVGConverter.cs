using Svg;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Ink;
using System.Windows.Markup;
using System.Xml.Linq;

namespace PolyPaint.Convertisseurs
{
    static class SVGConverter
    {
        public static string ConvertDrawingToSVG(StrokeCollection traits)
        {

            var svg = new SvgDocument();
            var colorServer = new SvgColourServer(System.Drawing.Color.Black);

            var group = new SvgGroup { Fill = colorServer, Stroke = colorServer };
            svg.Children.Add(group);

            foreach (var stroke in traits)
            {
                var geometry = stroke.GetGeometry(stroke.DrawingAttributes).GetOutlinedPath‌​Geometry();

                var s = XamlWriter.Save(geometry);

                if (!string.IsNullOrEmpty(s))
                {
                    var element = XElement.Parse(s);

                    var data = element.Attribute("Figures")?.Value;

                    if (!string.IsNullOrEmpty(data))
                    {
                        group.Children.Add(new SvgPath
                        {
                            PathData = SvgPathBuilder.Parse(data),
                            Fill = colorServer,
                            Stroke = colorServer
                        });
                    }
                }
            }
            return group.GetXML();
        }
    }
}
