using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Prototype_Lourd
{
    class Message
    {
        public string username;
        public string body;
        public string timestamp;

        public Message()
        {
            username = "";
            body = "";
            timestamp = "";
        }

        public Message(string a, string m, string t)
        {
            username = a;
            body = m;
            timestamp = t;
        }
    }
}
