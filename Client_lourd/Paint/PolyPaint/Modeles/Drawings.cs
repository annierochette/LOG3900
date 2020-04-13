using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PolyPaint.Modeles
{
    class Drawings
    {
        [JsonProperty]
        public List<Game> drawings { get; set; }
    }
}
