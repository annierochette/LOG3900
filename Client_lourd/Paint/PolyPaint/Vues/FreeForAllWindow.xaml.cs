using System;
using System.Windows;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Controls.Primitives;
using PolyPaint.VueModeles;
using System.Windows.Controls;
using System.Windows.Ink;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;

namespace PolyPaint.Vues
{
    /// <summary>
    /// Logique d'interaction pour FenetreDessin.xaml
    /// </summary>
    public partial class FreeForAllWindow : UserControl
    {

        private MouseEventArgs z;
        public FreeForAllWindow()
        {
            InitializeComponent();

        }


        // Pour gérer les points de contrôles.
        private void GlisserCommence(object sender, DragStartedEventArgs e)
        {
            (sender as Thumb).Background = Brushes.Black;
        }


        private void GlisserTermine(object sender, DragCompletedEventArgs e) { (sender as Thumb).Background = Brushes.White; }
        private void GlisserMouvementRecu(object sender, DragDeltaEventArgs e)
        {
            String nom = (sender as Thumb).Name;
            if (nom == "horizontal" || nom == "diagonal") colonne.Width = new GridLength(Math.Max(32, colonne.Width.Value + e.HorizontalChange));
            if (nom == "vertical" || nom == "diagonal") ligne.Height = new GridLength(Math.Max(32, ligne.Height.Value + e.VerticalChange));

        }

        private void StrokeColl(object sender, EventArgs e)
        {
            Point s = z.GetPosition(surfaceDessin);
            StrokeCollection strokesList = surfaceDessin.Strokes;
            foreach (var singleStroke in strokesList)
            {
                foreach (StylusPoint sp in singleStroke.StylusPoints)
                {
                    double X = sp.X;
                    double Y = sp.Y;

                    //Console.WriteLine(X + ", " + Y);
                }


            }

        }

        private void surfaceDessin_MouseMove(object sender, MouseEventArgs e)
        {
            Point p = e.GetPosition(surfaceDessin);
            z = e;
            surfaceDessin.Strokes.StrokesChanged += StrokeColl;
            if (surfaceDessin.IsMouseCaptured)
            {
                UploadStrokes(surfaceDessin.Strokes);
            }
        }

        void UploadStrokes(StrokeCollection strokes)
        {
            if (strokes.Count > 0)
            {
                MyCustomStrokes customStrokes = new MyCustomStrokes();
                customStrokes.StrokeCollection = new Point[strokes.Count][];

                for (int i = 0; i < strokes.Count; i++)
                {
                    customStrokes.StrokeCollection[i] =
                      new Point[surfaceDessin.Strokes[i].StylusPoints.Count];

                    for (int j = 0; j < strokes[i].StylusPoints.Count; j++)
                    {
                        customStrokes.StrokeCollection[i][j] = new Point();
                        customStrokes.StrokeCollection[i][j].X =
                                              strokes[i].StylusPoints[j].X;
                        customStrokes.StrokeCollection[i][j].Y =
                                              strokes[i].StylusPoints[j].Y;
                        Console.WriteLine(customStrokes.StrokeCollection[i][j]);
                    }

                }

                //Serialize our "strokes"
                MemoryStream ms = new MemoryStream();
                BinaryFormatter bf = new BinaryFormatter();
                bf.Serialize(ms, customStrokes);

                try
                {

                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.Message);
                }
            }
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

        private void MessageBoxControl_Loaded(object sender, System.Windows.RoutedEventArgs e)
        {

        }

        private void TimerControl_Loaded(object sender, RoutedEventArgs e)
        {

        }


    }
}
