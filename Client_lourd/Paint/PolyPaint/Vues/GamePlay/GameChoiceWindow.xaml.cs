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
using System.Net.Http;
using Newtonsoft.Json;
using PolyPaint.Utilitaires;

namespace PolyPaint.Vues
{
    /// <summary>
    /// Logique d'interaction pour UserControl1.xaml
    /// </summary>
    public partial class GameChoiceWindow : UserControl
    {
        private AppSocket socket = AppSocket.Instance;
        public GameChoiceWindow()
        {
            InitializeComponent();
        }

        public class gameCreated
        {

            [JsonProperty("username")]
            public string username { get; set; }

            [JsonProperty("password")]
            public string password { get; set; }
        }

        private void MessageBoxControl_Loaded(object sender, RoutedEventArgs e)
        {

        }

        private async void testing(object sender, RoutedEventArgs e) {
            //var HttpClient = new HttpClient();
            //var infos = new gameCreated
            //{
            //    username = username,
            //    password = password
            //};

            //var json = await Task.Run(() => JsonConvert.SerializeObject(infos));
            //var httpContent = new StringContent(json, Encoding.UTF8, "application/json");

            //  var res = await HttpClient.PostAsync("http://localhost:5050/players/login", httpContent);
            //if (res.Content != null)
            //{
            //    var responseContent = await res.Content.ReadAsStringAsync();
            //    Console.WriteLine(responseContent);

            //}
            //if (res.StatusCode.ToString() == "201")
            //{

            //}

            
 
        App.Current.Properties["gameName"] = "game1";
        socket.Emit("joinGame", "0");
        }
    }
}
