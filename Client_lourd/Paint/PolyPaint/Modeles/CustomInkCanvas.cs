using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using PolyPaint.Utilitaires;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;

namespace PolyPaint.Modeles
{
    public class CustomInkCanvas : System.Windows.Controls.InkCanvas
    {
        private static AppSocket socket = AppSocket.Instance;

        public CustomInkCanvas() : base()
        {
            this.DynamicRenderer = new CustomDynamicRenderer();
            //int index = this.StylusPlugIns.IndexOf(this.DynamicRenderer);
            //Console.WriteLine("Index: " + index + ", #plugIns: " + this.StylusPlugIns.Count);
            //this.StylusPlugIns.Insert(index, new DraftsmanPlugIn());
            //this.StylusPlugIns.RemoveAt(0);
            //Console.WriteLine("Index: " + index + ", #plugIns: " + this.StylusPlugIns.Count);
            socket.On("draw", (geometry) =>
            {
                Dispatcher.Invoke(() =>
                {
                    StylusPointCollection spc = new StylusPointCollection();
                    spc.Add((JsonConvert.DeserializeObject<StylusPoint>(geometry.ToString())));
                    Strokes.Add(new Stroke(spc));
                    //   Console.WriteLine("Geometry received: " + geometry);
                    //   Console.WriteLine("Children: " + Children.Count);            
                });
            });
        }

        private StylusPointCollection extractStylusPointsFromPath(string data)
        {
            StylusPointCollection spc = new StylusPointCollection();
            data = data.Remove(0, 3);
            data = data.Remove(data.Length - 1);
            data = data.Replace("C", ",").Replace(" ", ",");
            string[] splittedData = data.Split(',');
            Console.WriteLine("Data: " + data);
            for (int i = 0; i < splittedData.Length; i += 2)
            {
                spc.Add(new StylusPoint(Convert.ToDouble(splittedData[i]) + 10, Convert.ToDouble(splittedData[i + 1]) + 10));
            }

            return spc;
        }

        private class StylusPointConverter : JsonConverter
        {
            public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
            {
                var point = (StylusPoint)value;

                serializer.Serialize(
                    writer, new JObject { { "X", point.X }, { "Y", point.Y }, });
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
