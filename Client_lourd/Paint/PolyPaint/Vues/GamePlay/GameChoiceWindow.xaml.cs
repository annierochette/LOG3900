using Newtonsoft.Json;
using PolyPaint.Utilitaires;
using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;

namespace PolyPaint.Vues
{
    /// <summary>
    /// Logique d'interaction pour UserControl1.xaml
    /// </summary>
    public partial class GameChoiceWindow : UserControl
    {
        private AppSocket socket = AppSocket.Instance;
        public static gameList[] gamesUnstarted;
        public GameChoiceWindow()
        {
            InitializeComponent();
        }

        public gameList[] gameListTransfer
        {
            get
            {
                return gamesUnstarted;
            }
            set
            {
                gamesUnstarted = value;
            }
        }

        public class Player
        {

            [JsonProperty("name")]
            public string name { get; set; }

        }


        public class Joiner
        {

            [JsonProperty("name")]
            public string name { get; set; }

            [JsonProperty("score")]
            public int score { get; set; }

            [JsonProperty("_id")]
            public string _id { get; set; }

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

        public class gameList
        {

            [JsonProperty("name")]
            public string name { get; set; }

            [JsonProperty("status")]
            public string status { get; set; }

            [JsonProperty("_id")]
            public string _id { get; set; }

            [JsonProperty("games")]
            public string[] games { get; set; }

            [JsonProperty("type")]
            public string type { get; set; }

            [JsonProperty("players")]
            public List<Joiner> players { get; set; }

            [JsonProperty("__v")]
            public int __v { get; set; }
        }


        private void MessageBoxControl_Loaded(object sender, RoutedEventArgs e)
        {

        }

        private async void testing(object sender, RoutedEventArgs e)
        {
            var HttpClient = new HttpClient();
            HttpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im1hbm91Y2hlIiwiaWF0IjoxNTgzMzQyNTc2fQ.gWQpbS9nUt_Url6sDPgwBaAHLerd6XSc3k8lOq8sc7Y");
            var playerOne = new Player
            {
                name = "test"
            };

            List<Player> playersList = new List<Player>();
            playersList.Add(playerOne);

            var infos = new gameCreated
            {
                name = "game2",
                players = playersList,
                type = "FreeForAll"


            };

            var json = await Task.Run(() => JsonConvert.SerializeObject(infos));
            Console.WriteLine(json);
            var httpContent = new StringContent(json, Encoding.UTF8, "application/json");

            var res = await HttpClient.PostAsync(Constants.ADDR + "match/:type", httpContent);
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

        private void join(object sender, RoutedEventArgs e)
        {


        }

    }



}

