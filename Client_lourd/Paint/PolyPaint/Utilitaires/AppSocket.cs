using Quobject.SocketIoClientDotNet.Client;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;

namespace PolyPaint.Utilitaires
{
    public sealed class AppSocket
    {
        private Socket socket;
        

        private static readonly string URL = "http://127.0.0.1";
        private static readonly string PORT = ":5050";
        public InkCanvas canvas { get;  set; }

        public static AppSocket Instance { get; } = new AppSocket();

        private AppSocket()
        {
            socket = IO.Socket(URL + PORT);
        }

        ~AppSocket()
        {
            
            socket.Close();
        }

        public void On(string eventString, Action fn)
        {
            socket.On(eventString, fn);
        }

        public void On(string eventString, Action<object> fn)
        {
            socket.On(eventString, fn);
        }

        public void Emit(string eventString, params object[] args)
        {
            socket.Emit(eventString, args);
        }

       
    }
}
