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


namespace PolyPaint.Vues
{
    /// <summary>
    /// Logique d'interaction pour UserControl1.xaml
    /// </summary>
    public partial class JoiningGameWindow : UserControl
    {
        private AppSocket socket = AppSocket.Instance;
        private gameList[] gamesUnstarted = { };
        private bool isAccepted = false;

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

            [JsonProperty("turns")]
            public int turns { get; set; }
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
            HttpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im1hbm91Y2hlIiwiaWF0IjoxNTgzMzQyNTc2fQ.gWQpbS9nUt_Url6sDPgwBaAHLerd6XSc3k8lOq8sc7Y");




            var res = await HttpClient.GetAsync("http://localhost:5050/match/");
            if (res.Content != null)
            {
                var responseContent = await res.Content.ReadAsStringAsync();

                JavaScriptSerializer js = new JavaScriptSerializer();
                gamesUnstarted = js.Deserialize<gameList[]>(responseContent);
                //App.Current.Properties["games"] = gamesUnstarted;
                isAccepted = true;
                Console.WriteLine("on est rendu la");



            }
            if (res.StatusCode.ToString() == "201")
            {

            }

            int nbGames = gamesUnstarted.Length;
            for (int i = 0; i < nbGames; i++)
            {

                Button bt = new Button();
                bt.Name = gamesUnstarted[i].name;
                Console.WriteLine(bt.Name);
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
            string gameJoined = button.Name;
            App.Current.Properties["currentGame"] = gameJoined;
            socket.Emit("joinGame", gameJoined);

            var playerOne = new Joiner
            {
                name = "player2",
                score = 0
            };

            var HttpClient = new HttpClient();
            HttpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im1hbm91Y2hlIiwiaWF0IjoxNTgzMzQyNTc2fQ.gWQpbS9nUt_Url6sDPgwBaAHLerd6XSc3k8lOq8sc7Y");

            var json = await Task.Run(() => JsonConvert.SerializeObject(playerOne));
            Console.WriteLine(json);
            var httpContent = new StringContent(json, Encoding.UTF8, "application/json");



            var res = await HttpClient.PostAsync("http://localhost:5050/match/:name/player", httpContent);
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

        //public async Task<HttpResponseMessage> PatchAsync(this HttpClient client, string requestUri, HttpContent content)
        //{
        //    var method = new HttpMethod("PATCH");
        //    var request = new HttpRequestMessage(method, requestUri)
        //    {
        //        Content = content
        //    };

        //    var response = await client.SendAsync(request);
        //    return response;
        //}




    }
}
