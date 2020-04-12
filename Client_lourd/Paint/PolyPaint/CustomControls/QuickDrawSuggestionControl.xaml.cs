using Newtonsoft.Json;
using System.Net.Http;
using PolyPaint.VueModeles;
using System;
using System.ComponentModel;
using System.Threading.Tasks;
using System.Windows.Controls;
using System.Net.Http.Headers;
using System.Windows.Forms;
using PolyPaint.Modeles;
using System.Collections.Generic;
using System.Windows.Ink;

namespace PolyPaint.CustomControls

{
    /// <summary>
    /// Interaction logic for QuickDrawSuggestion.xaml
    /// </summary>
    public partial class QuickDrawSuggestionControl : System.Windows.Controls.UserControl
    {
        private List<Game> games = new List<Game>();
        private int gameShownIndex = 0;
        private bool fetching = false;

        public QuickDrawSuggestionControl()
        {   
            InitializeComponent();
            getRandomDrawings();
        }

        private void previous_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            if (gameShownIndex > 0)
            {
                gameShownIndex--;
                showNewGame(gameShownIndex);

                if (gameShownIndex <= 0)
                {
                    previous.Visibility = System.Windows.Visibility.Hidden;
                }
            }
        }

        private async void next_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            if (previous.Visibility == System.Windows.Visibility.Hidden)
            {
                previous.Visibility = System.Windows.Visibility.Visible;
            }
            if (gameShownIndex >= games.Count - 1)
            {
                if (!fetching)
                {
                    gameShownIndex++;
                    getRandomDrawings();
                }
            } else
            {
                gameShownIndex++;
                showNewGame(gameShownIndex);
            }

        }

        private async void getRandomDrawings() {
            try
            {
                fetching = true;
                var HttpClient = new HttpClient();
                HttpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im1hbm91Y2hlIiwiaWF0IjoxNTgzMzQyNTc2fQ.gWQpbS9nUt_Url6sDPgwBaAHLerd6XSc3k8lOq8sc7Y");

                var res = await HttpClient.GetAsync("http://localhost:5050/quickdraw/");
                if (res.Content != null)
                {
                    var responseContent = await res.Content.ReadAsStringAsync();

                    Drawings newDrawings = JsonConvert.DeserializeObject<Drawings>(responseContent);
                    games.AddRange(newDrawings.drawings);
                    showNewGame(gameShownIndex);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            fetching = false;
        }
        private void showNewGame(int index)
        {
            if (index >= games.Count)
            {
                Console.WriteLine("Veuillez patientez quelques instants");
            } else
            {
                Game game = games[index];
                inkPres.Strokes.Clear();
                inkPres.Strokes.Add(game.ResizedStrokes(inkPresBorder.ActualWidth, inkPresBorder.ActualHeight));
                wordToGuess.Content = game.word;
            }
        }

        public string getWord()
        {
            if (gameShownIndex >= games.Count)
            {
                gameShownIndex = games.Count - 1;
            }
            return games[gameShownIndex].word;
        }

        public StrokeCollection getGameStrokes()
        {
            if (gameShownIndex >= games.Count)
            {
                gameShownIndex = games.Count - 1;
            }
            return games[gameShownIndex].CollapsedStrokes();
        }
    }
}
