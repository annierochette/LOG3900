using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;

namespace PolyPaint.Convertisseurs
{
    class StylusPointConverter: JsonConverter
    {
        public override void WriteJson(
            JsonWriter writer, object value, JsonSerializer serializer)
        {
            var point = (StylusPoint)value;

            serializer.Serialize(
                writer, new JObject { { "x", point.X }, { "y", point.Y } });
        }

        public override object ReadJson(
            JsonReader reader, Type objectType, object existingValue,
            JsonSerializer serializer)
        {
            var jObject = serializer.Deserialize<JObject>(reader);

            return new StylusPoint((double)jObject["x"], (double)jObject["y"]);
        }

        public override bool CanConvert(Type objectType)
        {
            return objectType == typeof(StylusPoint);
        }
    }
}

