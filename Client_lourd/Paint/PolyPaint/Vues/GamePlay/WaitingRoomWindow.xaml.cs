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
using Quobject.SocketIoClientDotNet.Client;


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
            string gameName = (string)App.Current.Properties["gameName"];
            socket.Emit("joinGame", gameName);
            

        }

        private void MessageBoxControl_Loaded(object sender, RoutedEventArgs e)
        {

        }

        private void voir(object sender, RoutedEventArgs e) {
            Console.WriteLine("Voici le mot:");
            string prop = (string)App.Current.Properties["gameName"];
            Console.WriteLine(prop);
            App.Current.Properties["gameName"] = "partie2";
             prop = (string)App.Current.Properties["gameName"];
            Console.WriteLine(prop);
            
        }
    }
}
