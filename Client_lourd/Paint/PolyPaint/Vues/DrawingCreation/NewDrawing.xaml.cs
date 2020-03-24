using Newtonsoft.Json;
using PolyPaint.VueModeles;
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
using System.Windows.Input;
using System.Windows.Interactivity;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.Windows.Threading;

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

        private void save_form(object sender, RoutedEventArgs e)
        {

            inkCanvas.Visibility = Visibility.Visible;
            NewDrawingForm.Visibility = Visibility.Hidden;

            save.Visibility = Visibility.Hidden;
            confirm.Visibility = Visibility.Visible;

            cancel_button.Visibility = Visibility.Hidden;
            back_button.Visibility = Visibility.Visible;
        }

        private void back_button_Click(object sender, RoutedEventArgs e)
        {
            inkCanvas.Visibility = Visibility.Hidden;
            NewDrawingForm.Visibility = Visibility.Visible;

            save.Visibility = Visibility.Visible;
            confirm.Visibility = Visibility.Hidden;

            cancel_button.Visibility = Visibility.Visible;
            back_button.Visibility = Visibility.Hidden;
        }

        private void confirm_drawing(object sender, RoutedEventArgs e)
        {
            ((DrawingWindowViewModel)(this.DataContext)).AfficherTraitsClassique();
            //strokes = ((DrawingWindowViewModel)(this.DataContext)).Traits.Clone();
            //StylusPointCollection points = new StylusPointCollection();

            //foreach (Stroke stroke in strokes)
            //{
            //    points.Add(stroke.StylusPoints);

            //    StylusPointCollection first = new StylusPointCollection();
            //    first.Add(points[0]);
            //    Stroke newStrokes = new Stroke(first);
            //    DispatcherTimer timer = new DispatcherTimer();
            //    timer.Interval = TimeSpan.FromMilliseconds(10);
            //    timer.Start();
            //    int index = 1;
            //    timer.Tick += (s, a) =>
            //    {
            //        newStrokes.StylusPoints.Insert(index, points[index]);
            //        if (!inkPresenter.Strokes.Contains(newStrokes))
            //            inkPresenter.Strokes.Add(newStrokes);
            //        index++;

            //        if (index >= points.Count) timer.Stop();
            //    };

            //};

            inkPresenterBorder.Visibility = Visibility.Visible;
            inkCanvas.Visibility = Visibility.Hidden;

            send.Visibility = Visibility.Visible;
            confirm.Visibility = Visibility.Hidden;

            modifyDrawing_button.Visibility = Visibility.Visible;
            back_button.Visibility = Visibility.Hidden;
        }


    private void modifyDrawing_button_Click(object sender, RoutedEventArgs e)
        {
            inkPresenterBorder.Visibility = Visibility.Hidden;
            inkCanvas.Visibility = Visibility.Visible;

            send.Visibility = Visibility.Hidden;
            confirm.Visibility = Visibility.Visible;

            modifyDrawing_button.Visibility = Visibility.Hidden;
            back_button.Visibility = Visibility.Visible;

            if (((DrawingWindowViewModel)(DataContext)).Traits.Count != 0)
                ((DrawingWindowViewModel)(DataContext)).Traits.Clear();
            if (((DrawingWindowViewModel)(DataContext)).NouveauxTraits.Count != 0)
                ((DrawingWindowViewModel)(DataContext)).NouveauxTraits.Clear();
            strokes.Clear();
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

                strokes.Clear();
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
            if (!string.IsNullOrWhiteSpace(Clue.Text)) { 
            ListOfClues.Items.Add(Clue.Text);
            }
            Clue.Text = "";
        }

  
    }
}

