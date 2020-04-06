﻿using System;
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
using System.Net.Http.Headers;

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

        public class Player
        {

            [JsonProperty("name")]
            public string name { get; set; }

        }

        

        public class gameCreated
        {

            [JsonProperty("name")]
            public string name { get; set; }

            [JsonProperty("players")]
            public List<Player> players { get; set; }

            [JsonProperty("type")]
            public string type { get; set; }
        }

        private void MessageBoxControl_Loaded(object sender, RoutedEventArgs e)
        {

        }

        private async void testing(object sender, RoutedEventArgs e) {
            var HttpClient = new HttpClient();
            HttpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im1hbm91Y2hlIiwiaWF0IjoxNTgzMzQyNTc2fQ.gWQpbS9nUt_Url6sDPgwBaAHLerd6XSc3k8lOq8sc7Y");
            var playerOne = new Player {
                name = "test"
            };

            List<Player> playersList = new List<Player>();
            playersList.Add(playerOne); 

            var infos = new gameCreated
            {
                name = "game1",
                players = playersList,
                type = "FreeForAll"

                
            };

            var json = await Task.Run(() => JsonConvert.SerializeObject(infos));
            Console.WriteLine(json);
            var httpContent = new StringContent(json, Encoding.UTF8, "application/json");
            
              var res = await HttpClient.PostAsync("http://localhost:5050/match/:type", httpContent);
            if (res.Content != null)
            {
                var responseContent = await res.Content.ReadAsStringAsync();
                //Console.WriteLine(responseContent);
                App.Current.Properties["gameName"] = "game1";

            }
            if (res.StatusCode.ToString() == "201")
            {

            }



            App.Current.Properties["gameName"] = "game1";
            socket.Emit("createGame", "0");
        }
    }
}
