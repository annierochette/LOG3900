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
                writer, new JObject { { "X", point.X }, { "Y", point.Y } });
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

