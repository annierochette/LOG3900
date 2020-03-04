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
            additionalTextBoxRow = 4;
        }

        private void add_textBox(object sender, RoutedEventArgs e)
        {
            if (additionalTextBoxRow < 7) {
            TextBox txtb = new TextBox();

            txtb.Text = "Indice";

            txtb.Height = 40;

            txtb.SetValue(Grid.RowProperty, additionalTextBoxRow);

            txtb.SetValue(Grid.ColumnProperty, 3);

            txtb.SetValue(Grid.ColumnSpanProperty, 2);

            additionalTextBoxRow++;

            LayoutRoot.Children.Add(txtb);
            }
        }


        //void UploadStrokes(StrokeCollection strokes)
        //{
        //    if (strokes.Count > 0)
        //    {
        //        MyCustomStrokes customStrokes = new MyCustomStrokes();
        //        customStrokes.StrokeCollection = new Point[strokes.Count][];

        //        //for (int i = 0; i < strokes.Count; i++)
        //        //{
        //        //    customStrokes.StrokeCollection[i] =
        //        //      new Point[this.MyInkCanvas.Strokes[i].StylusPoints.Count];

        //        //    for (int j = 0; j < strokes[i].StylusPoints.Count; j++)
        //        //    {
        //        //        customStrokes.StrokeCollection[i][j] = new Point();
        //        //        customStrokes.StrokeCollection[i][j].X =
        //        //                              strokes[i].StylusPoints[j].X;
        //        //        customStrokes.StrokeCollection[i][j].Y =
        //        //                              strokes[i].StylusPoints[j].Y;
        //        //    }
        //        //}

        //        //Serialize our "strokes"
        //        MemoryStream ms = new MemoryStream();
        //        BinaryFormatter bf = new BinaryFormatter();
        //        bf.Serialize(ms, customStrokes);


        //    }
        //}

        private async void SendNewGame(object sender, RoutedEventArgs e)
        {
            var HttpClient = new HttpClient();
            var infos = new NewGame
            {
                Name = Word.Text,
                Clue = Indice.Text
            };
            var json = await Task.Run(() => JsonConvert.SerializeObject(infos));
            var httpContent = new StringContent(json, Encoding.UTF8, "application/json");

            var res = await HttpClient.PostAsync("http://localhost:5050/game/:id", httpContent);
            if (res.Content != null)
            {
                var responseContent = await res.Content.ReadAsStringAsync();
                Console.WriteLine(responseContent);

            }
        }

        public class NewGame
        {

            [JsonProperty("name")]
            public string Name { get; set; }

            [JsonProperty("clue")]
            public string Clue { get; set; }

            [JsonProperty("drawing")]
            public ByteArrayContent Drawing { get; set; }
        }

        private void MessageBox_Loaded(object sender, RoutedEventArgs e)
        {

        }

        //public sealed class MyCustomStrokes
        //{
        //    public MyCustomStrokes() { }

        //    public Point[][] StrokeCollection;
        //}
    } 
    
}
