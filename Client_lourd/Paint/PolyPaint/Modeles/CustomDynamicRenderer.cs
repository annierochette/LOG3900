using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
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
        private StylusPointCollection collectedPoints = new StylusPointCollection();
        

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
            collectedPoints.Add(stylusPoints.Reformat(collectedPoints.Description));
            if (collectedPoints.Count >= 25)
            {
                socket.Emit("draw", "General", JsonConvert.SerializeObject(collectedPoints.ElementAt(0), new StylusPointConverter()));
                collectedPoints.Clear();
            }
            base.OnDraw(drawingContext, stylusPoints, geometry,fillBrush);
        }

        //private void appendGeometry(string toAppend)
        //{
        //    path = path.Remove(path.Length - 1);
        //    path += toAppend.Remove(0, 3);
        //}

        private class StylusPointConverter : JsonConverter
        {
            public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
            {
                var point = (StylusPoint)value;

                serializer.Serialize(
                    writer, new JObject { { "X", point.X }, { "Y", point.Y },  });
            }

            public override object ReadJson(
                JsonReader reader, Type objectType, object existingValue,
                JsonSerializer serializer)
            {
                var jObject = serializer.Deserialize<JObject>(reader);

                return new StylusPoint((double)jObject["X"], (double)jObject["Y"]);
            }

            public override bool CanConvert(Type objectType)
            {
                return objectType == typeof(StylusPoint);
            }

        }

    }
}
