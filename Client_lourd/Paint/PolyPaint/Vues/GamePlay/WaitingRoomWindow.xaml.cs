using System;
using PolyPaint.Utilitaires;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using PolyPaint.VueModeles;
using Quobject.SocketIoClientDotNet.Client;
using PolyPaint.Modeles;


namespace PolyPaint.Vues
{
    /// <summary>
    /// Logique d'interaction pour GameStarterWindow.xaml
    /// </summary>
    public partial class WaitingRoomWindow : UserControl
    {
        
        private AppSocket socket = AppSocket.Instance;
        public WaitingRoomWindow()
        {

            InitializeComponent();
            string gameName = "";
            gameName = Global.GameName;
            
            Console.WriteLine("WaitingRoom: " +gameName);
            Console.WriteLine("waiting room: " + Application.Current.Properties["gameName"]);
            Console.WriteLine("token: " + User.Instance.Token);
            //socket.On("joinGame", gameName);

       
        }


        private void voir(object sender, RoutedEventArgs e) {
            
            
        }

        private void assignView(object sender, System.EventArgs e)
        {

            socket.Emit("startMatch", Global.GameName);
        }



    }
}
