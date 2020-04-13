using System;
using System.Collections.Generic;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Media;
using System.Net.Http;
using Newtonsoft.Json;
using PolyPaint.Utilitaires;
using System.Net.Http.Headers;
using System.Web.Script.Serialization;
using PolyPaint.VueModeles;
using System.Text;
using System.Threading.Tasks;
using PolyPaint.Modeles;
using System.Diagnostics;


namespace PolyPaint.Vues
{
    /// <summary>
    /// Logique d'interaction pour UserControl1.xaml
    /// </summary>
    public partial class JoiningGameWindow : UserControl
    {
        private AppSocket socket = AppSocket.Instance;
        private gameList[] gamesUnstarted = { };

        public class Player
        {

            [JsonProperty("name")]
            public string name { get; set; }

        }

        public class Players
        {

            [JsonProperty("players")]
            public Joiner players { get; set; }

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
        public JoiningGameWindow()
        {
            InitializeComponent();
            Loaded += GetGames;


        }

        private async void GetGames(object sender, RoutedEventArgs e)
        {
            Console.WriteLine("Nous sommes la");
            var HttpClient = new HttpClient();
            HttpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", User.instance.Token);




            var res = await HttpClient.GetAsync(Constants.ADDR + "match/");
            Console.WriteLine(res.StatusCode.ToString());
            if (res.Content != null)
            {
                var responseContent = await res.Content.ReadAsStringAsync();
                Console.WriteLine(responseContent); 
                JavaScriptSerializer js = new JavaScriptSerializer();
                gamesUnstarted = js.Deserialize<gameList[]>(responseContent);
                //App.Current.Properties["games"] = gamesUnstarted;
                Console.WriteLine("on est rendu la");



            }
            if (res.StatusCode.ToString() == "201")
            {

            }

            int nbGames = gamesUnstarted.Length;
            for (int i = 0; i < nbGames; i++)
            {

                Button bt = new Button();
                //bt.Name = gamesUnstarted[i].name;
                //Console.WriteLine(bt.Name);
                bt.Content = gamesUnstarted[i].name;
                bt.Click += joinGame;

                bt.Height = 50;
                bt.Width = 220;
                bt.Foreground = Brushes.White;

                Thickness margin = new Thickness(20);
                bt.Margin = margin;

                // FontSize="24" FontFamily="Ink Free"
                bt.FontSize = 24;
                FontFamily font = new FontFamily("Ink Free");
                bt.FontFamily = font;
                bt.Background = Brushes.Orange;

                //Command="{Binding GoToWaitingRoom}"
                Binding test = new Binding("GoToWaitingRoom");

                JoiningGameViewModel testi = new JoiningGameViewModel();
                bt.Command = testi.GoToWaitingRoom;
                gameStack.Children.Add(bt);
            }

        }


        private async void joinGame(object sender, RoutedEventArgs e)
        {

            var button = (Button)sender;
            string gameJoined = (string)button.Content;
            App.Current.Properties["currentGame"] = gameJoined;
            socket.Emit("joinGame", gameJoined);

            var playerOne = new Joiner
            {
                name = User.instance.Username,
                score = 0,
                _id = User.instance.Id
            };

            var playerList = new Players
            {
                players = playerOne
            };

            var HttpClient = new HttpClient();
            HttpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", User.instance.Token);

            var json = await Task.Run(() => JsonConvert.SerializeObject(playerList));
            Console.WriteLine(json);
            var httpContent = new StringContent(json, Encoding.UTF8, "application/json");



            //var res = await HttpClient.PostAsync(Constants.ADDR + "match/:name/player", httpContent);
            var res = await PatchAsync(HttpClient, new Uri(Constants.ADDR + "match/:name/player"), httpContent);
            if (res.Content != null)
            {
                
                var responseContent = await res.Content.ReadAsStringAsync();

                Console.WriteLine(responseContent);
                Console.WriteLine("on est rendu la");



            }
            if (res.StatusCode.ToString() == "201")
            {

            }

        }

        public static async Task<HttpResponseMessage> PatchAsync(HttpClient client, Uri requestUri, HttpContent iContent)
        {
            var method = new HttpMethod("PATCH");
            var request = new HttpRequestMessage(method, requestUri)
            {
                Content = iContent
            };

            HttpResponseMessage response = new HttpResponseMessage();
            try
            {
                response = await client.SendAsync(request);
            }
            catch (TaskCanceledException e)
            {
                Debug.WriteLine("ERROR: " + e.ToString());
            }

            return response;
        }

        



    }
}
