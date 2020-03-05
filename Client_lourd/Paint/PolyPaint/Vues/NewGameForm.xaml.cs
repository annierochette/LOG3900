using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace PolyPaint.Vues
{
    /// <summary>
    /// Interaction logic for NewGameForm.xaml
    /// </summary>
    public partial class NewGameForm : UserControl
    {
        public int additionalTextBoxRow;
        public NewGameForm()
        {
            InitializeComponent();
        }

        private string[] getClues()
        {
            string[] clues = new string[2];
            for (int i = 0; i < ListOfClues.Items.Count; i++)
            {
                clues[i] = ListOfClues.Items[i].ToString();
            }

            return clues;
        }


        private async void SendNewGame(object sender, RoutedEventArgs e)
        {
            string[] clues = getClues();


            StylusPoint segment1Start = new StylusPoint(200, 110);
            StylusPoint segment1End = new StylusPoint(185, 150);
            StylusPoint segment2Start = new StylusPoint(185, 150);
            StylusPoint segment2End = new StylusPoint(135, 150);
            StylusPoint segment3Start = new StylusPoint(135, 150);
            StylusPoint segment3End = new StylusPoint(175, 180);
            StylusPoint segment4Start = new StylusPoint(175, 180);
            StylusPoint segment4End = new StylusPoint(160, 220);
            StylusPoint segment5Start = new StylusPoint(160, 220);
            StylusPoint segment5End = new StylusPoint(200, 195);
            StylusPoint segment6Start = new StylusPoint(200, 195);
            StylusPoint segment6End = new StylusPoint(240, 220);
            StylusPoint segment7Start = new StylusPoint(240, 220);
            StylusPoint segment7End = new StylusPoint(225, 180);
            StylusPoint segment8Start = new StylusPoint(225, 180);
            StylusPoint segment8End = new StylusPoint(265, 150);
            StylusPoint segment9Start = new StylusPoint(265, 150);
            StylusPoint segment9End = new StylusPoint(215, 150);
            StylusPoint segment10Start = new StylusPoint(215, 150);
            StylusPoint segment10End = new StylusPoint(200, 110);

            StylusPointCollection strokePoints = new StylusPointCollection();

            strokePoints.Add(segment1Start);
            strokePoints.Add(segment1End);
            strokePoints.Add(segment2Start);
            strokePoints.Add(segment2End);
            strokePoints.Add(segment3Start);
            strokePoints.Add(segment3End);
            strokePoints.Add(segment4Start);
            strokePoints.Add(segment4End);
            strokePoints.Add(segment5Start);
            strokePoints.Add(segment5End);
            strokePoints.Add(segment6Start);
            strokePoints.Add(segment6End);
            strokePoints.Add(segment7Start);
            strokePoints.Add(segment7End);
            strokePoints.Add(segment8Start);
            strokePoints.Add(segment8End);
            strokePoints.Add(segment9Start);
            strokePoints.Add(segment9End);
            strokePoints.Add(segment10Start);
            strokePoints.Add(segment10End);

            Stroke newStroke = new Stroke(strokePoints);
            StrokeCollection strokes = new StrokeCollection();
            strokes.Add(newStroke);

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
                        clues = new string[1] { "scintille"},
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
            public string[] clues { get; set; }

            [JsonProperty("drawing")]
            public byte[] data{ get; set; }
        }
        [Serializable]
        public sealed class MyCustomStrokes
        {
            public MyCustomStrokes() { }

            /// <SUMMARY>
            /// The first index is for the stroke no.
            /// The second index is for the keep the 2D point of the Stroke.
            /// </SUMMARY>
            public Point[][] StrokeCollection;
        }

        private void MessageBox_Loaded(object sender, RoutedEventArgs e)
        {
          

        }

        private void button_Click(object sender, RoutedEventArgs e)
        {
            ListOfClues.Items.Add(Clue.Text);
            Clue.Text = "";
        }
    } 
    
}
