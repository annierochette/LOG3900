using CsPotrace;
using Newtonsoft.Json;
using PolyPaint.Convertisseurs;
using PolyPaint.Utilitaires;
using PolyPaint.VueModeles;
using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Forms;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Threading;
using Point = System.Windows.Point;

namespace PolyPaint.Vues
{

    public partial class NewDrawing : System.Windows.Controls.UserControl
    {
        StrokeCollection strokes = new StrokeCollection();
        public String Svg ;
        bool[,] Matrix;
        ArrayList ListOfCurveArray;
        Bitmap Bitmap;


        public NewDrawing()
        {
            InitializeComponent();
            IsWordWritten = false;

        }

        public bool IsWordWritten { get; set; }
        public bool HasAtleastOneClue { get; set; }

        private void DrawingWindow_Loaded(object sender, RoutedEventArgs e)
        {

        }

        private void textChangedEventHandler(object sender, TextChangedEventArgs args)
        {
            if (!string.IsNullOrWhiteSpace(Word.Text))
            {
                IsWordWritten = true;
                if (HasAtleastOneClue)
                {
                    save.IsEnabled = true;
                    importer.IsEnabled = true;
                    if (!string.IsNullOrWhiteSpace(Svg) || strokes.Count > 0)
                    {
                        sendGame.IsEnabled = true;
                    }
                }
            }
            else
            {
                IsWordWritten = false;
                save.IsEnabled = false;
            }
        }


        private void save_form(object sender, RoutedEventArgs e)
        {

            inkCanvas.Visibility = Visibility.Visible;
            NewDrawingForm.Visibility = Visibility.Collapsed;

            save.Visibility = Visibility.Collapsed;
            confirm.Visibility = Visibility.Visible;

            cancel_button.Visibility = Visibility.Collapsed;
            back_button.Visibility = Visibility.Visible;

            quickdraw.Visibility = Visibility.Collapsed;
            importer.Visibility = Visibility.Collapsed;
            sendGame.Visibility = Visibility.Collapsed;
        }

        private void back_button_Click(object sender, RoutedEventArgs e)
        {
            inkCanvas.Visibility = Visibility.Collapsed;
            NewDrawingForm.Visibility = Visibility.Visible;

            save.Visibility = Visibility.Visible;
            confirm.Visibility = Visibility.Collapsed;

            cancel_button.Visibility = Visibility.Visible;
            back_button.Visibility = Visibility.Collapsed;

            importer.Visibility = Visibility.Visible;
            quickdraw.Visibility = Visibility.Visible;
            quickdrawPage.Visibility = Visibility.Collapsed;
            ImageImport.Visibility = Visibility.Collapsed;

            sendGame.Visibility = Visibility.Visible;

            cancel_button2.Visibility = Visibility.Collapsed;
            save_button.Visibility = Visibility.Collapsed;
            updateForm.Visibility = Visibility.Collapsed;
        }


        private void confirm_drawing(object sender, RoutedEventArgs e)
        {
            //((DrawingWindowViewModel)(this.DataContext)).AfficherTraitsClassique();
            afficherTraitsClassique();
            //afficherTraitsAléatoire();

            inkPresenterBorder.Visibility = Visibility.Visible;
            inkCanvas.Visibility = Visibility.Hidden;

            send.Visibility = Visibility.Visible;
            confirm.Visibility = Visibility.Hidden;

            modifyDrawing_button.Visibility = Visibility.Visible;
            back_button.Visibility = Visibility.Hidden;
        }

        private void afficherTraitsClassique()
        {

            strokes = ((DrawingWindowViewModel)(DataContext)).Traits;
            var tasks = new List<Task<(int Index, bool IsDone)>>();


            foreach (Stroke stroke in strokes)
            {


                StylusPointCollection points = new StylusPointCollection();
                Timer timer = new Timer();
                timer.Interval = 10;
                timer.Start();

                int index = 0;

                timer.Tick += (s, a) =>
                {

                    StylusPoint point = stroke.StylusPoints[index];
                    var x = (float)point.X;
                    var y = (float)point.Y;

                    points.Add(new StylusPoint(x, y));

                    inkPresenter.Strokes.Add(new Stroke(points));

                    index++;
                    if (index >= stroke.StylusPoints.Count)
                    {
                        timer.Stop();

                    }
                };


            }

        }

        //private void afficherTraitsClassique()
        //{
        //    strokes = ((DrawingWindowViewModel)(DataContext)).Traits;
        //    StylusPointCollection points = new StylusPointCollection();


        //    foreach (Stroke stroke in strokes)
        //    {
        //        points.Add(stroke.StylusPoints);
        //        StylusPointCollection first = new StylusPointCollection();
        //        first.Add(points[0]);
        //        Stroke newStroke = new Stroke(first);
        //        DispatcherTimer timer = new DispatcherTimer();
        //        timer.Interval = TimeSpan.FromMilliseconds(10);
        //        timer.Start();
        //        int index = 0;
        //        timer.Tick += (s, a) =>
        //                {

        //                    newStroke.StylusPoints.Insert(index, points[index]);
        //                    if (!inkPresenter.Strokes.Contains(newStroke))
        //                    {
        //                        inkPresenter.Strokes.Add(newStroke);
        //                    }
        //                    index++;
        //                    if (index >= points.Count) timer.Stop();
        //                };
        //    };


        //}

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

            confirm.IsEnabled = false;
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


        private void SendNewGame(object sender, RoutedEventArgs e)
        {
            quickdrawPage.Visibility = Visibility.Collapsed;
            NewDrawingForm.Visibility = Visibility.Visible;

            save.Visibility = Visibility.Visible;
            confirm.Visibility = Visibility.Collapsed;
            send.Visibility = Visibility.Collapsed;
            inkCanvas.Visibility = Visibility.Hidden;
            modifyDrawing_button.Visibility = Visibility.Collapsed;

            cancel_button.Visibility = Visibility.Visible;
            back_button.Visibility = Visibility.Collapsed;
            quickdraw.Visibility = Visibility.Visible;
            importer.Visibility = Visibility.Visible;


            sendGame.Visibility = Visibility.Visible;
        }

        public class Game
        {

            [JsonProperty("name")]
            public string name { get; set; }

            [JsonProperty("clues")]
            public List<string> clues { get; set; }

            [JsonProperty("data")]
            public string data { get; set; }
        }
        [Serializable]
        public sealed class MyCustomStrokes
        {
            public MyCustomStrokes() { }
            public Point[][] StrokeCollection;
        }


        private void Add_clue(object sender, RoutedEventArgs e)
        {
            if (!string.IsNullOrWhiteSpace(Clue.Text)) { 
            ListOfClues.Items.Add(Clue.Text);
                HasAtleastOneClue = true;
                DeleteClue.IsEnabled = true;
                if (IsWordWritten)
                {
                    save.IsEnabled = true;
                    importer.IsEnabled = true;
                    if (!string.IsNullOrWhiteSpace(Svg) || strokes.Count > 0)
                    {
                        sendGame.IsEnabled = true;
                    }
                }
            }
            Clue.Text = "";
        }

      
        private void Delete_clue(object sender, EventArgs e)
        {

            if (ListOfClues.SelectedItems.Count != 0)
            {
                while (ListOfClues.SelectedIndex != -1)
                {
                    ListOfClues.Items.RemoveAt(ListOfClues.SelectedIndex);
                }
            }
            if (!ListOfClues.HasItems) { 
                DeleteClue.IsEnabled = false;
                save.IsEnabled = false;
                sendGame.IsEnabled = false;
                importer.IsEnabled = false;
            }

        }


        public event PropertyChangedEventHandler PropertyChanged = delegate { };

        protected void OnPropertyChanged(string property)
        {
            PropertyChanged(this, new PropertyChangedEventArgs(property));
        }

        private void inkCanvas_StrokeCollected(object sender, InkCanvasStrokeCollectedEventArgs e)
        {
            confirm.IsEnabled = true;
            sendGame.IsEnabled = false;
        }



        private void refreshMatrix()
        {
            if (Bitmap == null) return;
            Matrix = Potrace.BitMapToBinary(Bitmap, (int)contrastSlider.Value);
            refreshPicture();

        }

        private void ContrastSlider_Scroll(object sender, EventArgs e)
        {
            refreshMatrix();
            float p = 100 * (float)contrastSlider.Value / (float)255;

        }

        private void refreshPicture()
        {
            if (Matrix == null) return;
            Bitmap b = Potrace.BinaryToBitmap(Matrix, true);
            imgPhoto.Source = BitmapToImageSource(b);

        }

        private void vectorize()
        {

            ListOfCurveArray = new ArrayList();
            Potrace.turdsize = Convert.ToInt32(contrastSlider.Value);
            Potrace.curveoptimizing = true;
            Matrix = Potrace.BitMapToBinary(Bitmap, (int)contrastSlider.Value);
            Potrace.potrace_trace(Matrix, ListOfCurveArray);
            Bitmap s = Potrace.Export2GDIPlus(ListOfCurveArray, Bitmap.Width, Bitmap.Height);
            imgPhoto.Source = BitmapToImageSource(s);
            refreshMatrix();

        }

        BitmapImage BitmapToImageSource(Bitmap bitmap)
        {
            using (MemoryStream memory = new MemoryStream())
            {
                bitmap.Save(memory, System.Drawing.Imaging.ImageFormat.Bmp);
                memory.Position = 0;
                BitmapImage bitmapimage = new BitmapImage();
                bitmapimage.BeginInit();
                bitmapimage.StreamSource = memory;
                bitmapimage.CacheOption = BitmapCacheOption.OnLoad;
                bitmapimage.EndInit();

                return bitmapimage;
            }
        }

        private void btnLoad_Click(object sender, RoutedEventArgs e)
        {
            Microsoft.Win32.OpenFileDialog op = new Microsoft.Win32.OpenFileDialog();
            op.Title = "Select a picture";
            op.Filter = "All supported graphics|*.jpg;*.jpeg;*.png|" +
              "JPEG (*.jpg;*.jpeg)|*.jpg;*.jpeg|" +
              "Portable Network Graphic (*.png)|*.png" +
              "|BMP Windows Bitmap (*.bmp)|*.bmp";

            if (op.ShowDialog() == true)
            {
                imgPhoto.Source = new BitmapImage(new Uri(op.FileName));
                save_button.IsEnabled = true;
                ListOfCurveArray = null;
                if (Bitmap != null) Bitmap.Dispose();
                Bitmap = new Bitmap(op.FileName);
                refreshMatrix();
                vectorize();
                Svg = Potrace.Export2SVG(ListOfCurveArray, Bitmap.Width, Bitmap.Height);

                if (HasAtleastOneClue && !string.IsNullOrWhiteSpace(Word.Text))
                {
                    save.IsEnabled = true;
                }
            }
        }

        private void ImporterImage(object sender, RoutedEventArgs e)
        {
            NewDrawingForm.Visibility = Visibility.Collapsed;
            importer.Visibility = Visibility.Collapsed;
            save.Visibility = Visibility.Collapsed;
            cancel_button.Visibility = Visibility.Collapsed;
            quickdraw.Visibility = Visibility.Collapsed;
            back_button.Visibility = Visibility.Collapsed;
            sendGame.Visibility = Visibility.Collapsed;
            ImageImport.Visibility = Visibility.Visible;
            save_button.Visibility = Visibility.Visible;
            cancel_button2.Visibility = Visibility.Visible;
        }

        private void quickdraw_Click(object sender, RoutedEventArgs e)
        {
            quickdrawPage.Visibility = Visibility.Visible;
            NewDrawingForm.Visibility = Visibility.Collapsed;

            save.Visibility = Visibility.Collapsed;
            confirm.Visibility = Visibility.Collapsed;
            send.Visibility = Visibility.Collapsed;

            cancel_button.Visibility = Visibility.Collapsed;
            back_button.Visibility = Visibility.Visible;
            quickdraw.Visibility = Visibility.Collapsed;
            importer.Visibility = Visibility.Collapsed;

            sendGame.Visibility = Visibility.Collapsed;
            updateForm.Visibility = Visibility.Visible;
        }

        private async void sendGame_Click(object sender, RoutedEventArgs e)
        {
            List<string> clues = getClues();

            if (String.IsNullOrEmpty(Svg))
            {
                Svg = SVGConverter.ConvertDrawingToSVG(strokes);
            }

            try
            {
                var HttpClient = new HttpClient();
                var infos = new Game
                {
                    name = Word.Text,
                    clues = clues,
                    data = Svg
                };
                var json = await Task.Run(() => JsonConvert.SerializeObject(infos));
                var httpContent = new StringContent(json, Encoding.UTF8, "application/json");

                var res = await HttpClient.PostAsync(Constants.ADDR + "games", httpContent);
                if (res.Content != null)
                {
                    var responseContent = await res.Content.ReadAsStringAsync();
                    Console.WriteLine(responseContent);
                    System.Windows.Forms.MessageBox.Show("Image bien sauvegardée!", "Caption", MessageBoxButtons.OK, MessageBoxIcon.Information);
                }
            }
            catch (Exception ex)
            {
                System.Windows.Forms.MessageBox.Show(ex.Message);
            }

            strokes.Clear();
        }

        private void updateForm_Click(object sender, RoutedEventArgs e)
        {
            Word.Text = quickdrawPage.getWord();
            strokes = quickdrawPage.getGameStrokes();
            back_button_Click(sender, e);
        }
    }
}

