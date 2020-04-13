using Quobject.SocketIoClientDotNet.Client;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PolyPaint.Utilitaires
{
    public sealed class AppSocket
    {
        private Socket socket;        

        public static AppSocket Instance { get; } = new AppSocket();

        private AppSocket()
        {
            socket = IO.Socket(Constants.ADDR);
        }

        ~AppSocket()
        {
            socket.Disconnect();
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

        public void Close()
        {
            socket.Emit("disconnection");
            socket.Disconnect();
            socket.Close();
        }

       
    }
}
