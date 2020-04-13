using System;
using System.Windows;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Controls.Primitives;
using PolyPaint.VueModeles;
using System.Windows.Controls;
using System.Windows.Ink;
using PolyPaint.Utilitaires;


namespace PolyPaint.Vues
{
    /// <summary>
    /// Logique d'interaction pour FenetreDessin.xaml
    /// </summary>
    public partial class FreeForAllWindow : UserControl
    {
        private AppSocket socket;
        //private MouseEventArgs z;
        public FreeForAllWindow()
        {
            InitializeComponent();
            
            timer._end += onEndTimer;
            
        }

        public void onEndTimer(object sender, EventArgs e)
        {
            //socket.Emit("nextRound", Global.GameName);
            Console.WriteLine("fin de round");
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
            //Point s = z.GetPosition(surfaceDessin);
            //StrokeCollection strokesList = surfaceDessin.Strokes;
            //foreach (var singleStroke in strokesList)
            //{
            //    foreach (StylusPoint sp in singleStroke.StylusPoints)
            //    {
            //        double X = sp.X;
            //        double Y = sp.Y;

            //        //Console.WriteLine(X + ", " + Y);
            //    }
            //}

        }

        private void surfaceDessin_MouseMove(object sender, MouseEventArgs e)
        {
            //Point p = e.GetPosition(surfaceDessin);
            //z = e;
            //surfaceDessin.Strokes.StrokesChanged += StrokeColl;


        }

        private void MessageBoxControl_Loaded(object sender, System.Windows.RoutedEventArgs e)
        {

        }

        private void TimerControl_Loaded(object sender, RoutedEventArgs e)
        {
            
        }

        private void sendMessageOnEnter(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                answer(sender, e);
            }
        }

        private void answer(object sender, RoutedEventArgs e) {
            string answer = answerTextBox.Text;
            socket.Emit("answer", Global.GameName, answer);
            answerTextBox.Text = String.Empty;
            answerTextBox.Focus();

            //string wordToFind = word.Text;
            //if (answer == wordToFind)
            //{
            //    answerTextBox.IsEnabled = false;
            //    socket.Emit("answer", Global.GameName, answer);
            //    answerTextBox.Text = String.Empty;

            //}
            //else 
            //{
            //    answerTextBox.Text = String.Empty;
            //    answerTextBox.Focus();
            //}
        }


    }
}
