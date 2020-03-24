using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Http;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Ink;
using System.Windows.Media.Animation;

namespace PolyPaint.Vues
{
  
    public partial class NewDrawing : UserControl
    {
        StrokeCollection strokes = new StrokeCollection();

        public NewDrawing()
        {
            InitializeComponent();
        }

        private void DrawingWindow_Loaded(object sender, RoutedEventArgs e)
        {

        }

        private void MessageBoxControl_Loaded(object sender, RoutedEventArgs e)
        {

        }

        private void save_drawing(object sender, RoutedEventArgs e)
        {
            //var storyboard = new Storyboard();
            //var totalDuration = TimeSpan.FromSeconds(10);


            strokes = inkCanvas.Strokes.Clone();

            inkPresenter.Strokes = strokes;

            inkCanvas.Visibility = Visibility.Hidden;
            inkPresenterBorder.Visibility = Visibility.Visible;

            save_button.Visibility = Visibility.Hidden;
            confirm_button.Visibility = Visibility.Visible;

            modifyDrawing_button.Visibility = Visibility.Visible;
            cancel_button.Visibility = Visibility.Hidden;
        }

        private void modifyDrawing_button_Click(object sender, RoutedEventArgs e)
        {
            inkCanvas.Visibility = Visibility.Visible;
            inkPresenterBorder.Visibility = Visibility.Hidden;
            save_button.Visibility = Visibility.Visible;
            confirm_button.Visibility = Visibility.Hidden;
            modifyDrawing_button.Visibility = Visibility.Hidden;
            cancel_button.Visibility = Visibility.Visible;
        }

        private void confirm_drawing(object sender, RoutedEventArgs e)
        {
            inkPresenterBorder.Visibility = Visibility.Hidden;
            save_button.Visibility = Visibility.Hidden;
            confirm_button.Visibility = Visibility.Hidden;
            NewDrawingForm.Visibility = Visibility.Visible;
            confirm_data.Visibility = Visibility.Visible;

            modifyDrawing_button.Visibility = Visibility.Hidden;
            back_button.Visibility = Visibility.Visible;
        }

        private void back_button_Click(object sender, RoutedEventArgs e)
        {
            inkPresenterBorder.Visibility = Visibility.Visible;
            save_button.Visibility = Visibility.Visible;
            confirm_button.Visibility = Visibility.Visible;
            
            NewDrawingForm.Visibility = Visibility.Hidden;
            confirm_data.Visibility = Visibility.Hidden;

            modifyDrawing_button.Visibility = Visibility.Visible;
            back_button.Visibility = Visibility.Hidden;
        }   

        

        private List<string> getClues()
        {
            List<string> clues = new List<string>() ;

            foreach ( string item in ListOfClues.Items)
            {
                clues.Add(item);
               
            }

            return clues;
        }


        private async void SendNewGame(object sender, RoutedEventArgs e)
        {
            List<string> clues = getClues();

            if (strokes.Count > 0)
            {
                Console.WriteLine(strokes.Count);
          
                MyCustomStrokes customStrokes = new MyCustomStrokes();
                customStrokes.StrokeCollection = new Point[strokes.Count][];

                for (int i = 0; i < strokes.Count; i++)
                {
                    customStrokes.StrokeCollection[i] =
                      new Point[strokes[i].StylusPoints.Count];

                    for (int j = 0; j < strokes[i].StylusPoints.Count; j++)
                    {
                        customStrokes.StrokeCollection[i][j] = new Point();
                        customStrokes.StrokeCollection[i][j].X =
                                              strokes[i].StylusPoints[j].X;
                        customStrokes.StrokeCollection[i][j].Y =
                                              strokes[i].StylusPoints[j].Y;
                    }
                }


                MemoryStream ms = new MemoryStream();
                BinaryFormatter bf = new BinaryFormatter();
                bf.Serialize(ms, customStrokes);

                try
                {

                    var HttpClient = new HttpClient();
                    var infos = new Game
                    {
                        name = Word.Text,
                        clues = clues,
                        data = ms.GetBuffer()
                    };
                    var json = await Task.Run(() => JsonConvert.SerializeObject(infos));
                    var httpContent = new StringContent(json, Encoding.UTF8, "application/json");

                    var res = await HttpClient.PostAsync("http://localhost:5050/games", httpContent);
                    if (res.Content != null)
                    {
                        var responseContent = await res.Content.ReadAsStringAsync();
                        Console.WriteLine(responseContent);

                    }
                }

                catch (Exception ex)
                {
                    MessageBox.Show(ex.Message);
                }
            }
        }

        public class Game
        {

            [JsonProperty("name")]
            public string name { get; set; }

            [JsonProperty("clues")]
            public List<string> clues { get; set; }

            [JsonProperty("data")]
            public byte[] data { get; set; }
        }
        [Serializable]
        public sealed class MyCustomStrokes
        {
            public MyCustomStrokes() { }
            public Point[][] StrokeCollection;
        }


        private void add_clue(object sender, RoutedEventArgs e)
        {
            ListOfClues.Items.Add(Clue.Text);
            Clue.Text = "";
        }

  
    }
}

