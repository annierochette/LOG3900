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
using System.Web.Script.Serialization;
using PolyPaint.VueModeles;
using System.Windows.Data;
using PolyPaint.Modeles;

namespace PolyPaint.Vues
{
    /// <summary>
    /// Logique d'interaction pour UserControl1.xaml
    /// </summary>
    public partial class GameChoiceWindow : UserControl
    {
        private AppSocket socket = AppSocket.Instance;
        public static Match game;
        public string gameName;
        public GameChoiceWindow()
        {
            InitializeComponent();
        }

        

        public class Player
        {

            [JsonProperty("name")]
            public string name { get; set; }

        }

        public class Match
        {

            [JsonProperty("match")]
            public gameList match { get; set; }

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

            [JsonProperty("turns")]
            public int turns { get; set; }
        }


        private void MessageBoxControl_Loaded(object sender, RoutedEventArgs e)
        {

        }

        private async void testing(object sender, RoutedEventArgs e)
        {
            var HttpClient = new HttpClient();
            HttpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", User.instance.Token);
            var playerOne = new Player
            {
                name = "test"
            };

            List<Player> playersList = new List<Player>();
            playersList.Add(playerOne);

            var infos = new gameCreated
            {
                type = "FreeForAll"

            };

            var json = await Task.Run(() => JsonConvert.SerializeObject(infos));
            //Console.WriteLine(json);
            var httpContent = new StringContent(json, Encoding.UTF8, "application/json");

            var res = await HttpClient.PostAsync(Constants.ADDR + "match/:type", httpContent);
            if (res.Content != null)
            {
                var responseContent = await res.Content.ReadAsStringAsync();
                //Console.WriteLine(responseContent);
                JavaScriptSerializer js = new JavaScriptSerializer();
                game= js.Deserialize<Match>(responseContent);
                //Button bt = (Button)sender;
                //Console.WriteLine(game.match.name);
                Global.GameName = game.match.name;
                Application.Current.Properties["gameName"] = game.match.name;
                Console.WriteLine("gameChoice: " + Global.GameName);
                ((GameChoiceViewModel)(DataContext)).GiveAccess();
                socket.Emit("joinGame", (game.match.name, User.instance.Username));
            }
            if (res.StatusCode.ToString() == "201")
            {

            }

            
        }

        private void join(object sender, RoutedEventArgs e)
        {


        }

    }



}

