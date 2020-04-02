using System;
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

namespace PolyPaint.Vues
{
    /// <summary>
    /// Logique d'interaction pour GameStarterWindow.xaml
    /// </summary>
    public partial class WaitingRoomWindow : UserControl
    {
        private Socket socket;
        public WaitingRoomWindow()
        {
            InitializeComponent();
            socket = IO.Socket("http://localhost:5050");
            socket.Emit("joinGame", "0");
            socket.On("joinGame", (data) =>
            {
                Newtonsoft.Json.Linq.JObject obj = (Newtonsoft.Json.Linq.JObject)data;
                Newtonsoft.Json.Linq.JToken un = obj.GetValue("nbPlayers");
                Console.WriteLine(un);
                nbConnect.Text = (string)un;

            });

        }


        private void voir(object sender, RoutedEventArgs e) {
            Console.WriteLine("Voici le mot:");
            string prop = (string)App.Current.Properties["gameName"];
            Console.WriteLine(prop);
            App.Current.Properties["gameName"] = "partie2";
             prop = (string)App.Current.Properties["gameName"];
            Console.WriteLine(prop);
            
        }

        private void assignView(object sender, System.EventArgs e)
        {

            ((WaitingRoomViewModel)(DataContext)).assignGuessingView();
            //((WaitingRoomViewModel)(DataContext)).assignDrawingView();
        }



    }
}
