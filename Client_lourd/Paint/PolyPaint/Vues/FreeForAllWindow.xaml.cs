using System;
using System.Windows;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Controls.Primitives;
using PolyPaint.VueModeles;
using System.Windows.Controls;
using System.Windows.Ink;
using System.Windows.Data;
using System.Windows.Input.StylusPlugIns;
using PolyPaint.Utilitaires;

namespace PolyPaint.Vues
{
    /// <summary>
    /// Logique d'interaction pour FenetreDessin.xaml
    /// </summary>
    public partial class FreeForAllWindow : UserControl
    {
        private DynamicRenderer renderer = new DynamicRenderer();

        private MouseEventArgs z;

        private readonly int SIZE = 25;
        private StylusPointCollection spc = new StylusPointCollection();

        private AppSocket socket = AppSocket.Instance;

        public FreeForAllWindow()
        {
            InitializeComponent();
            DrawingAttributes inkDA = new DrawingAttributes();
            inkDA.Width = 50;
            inkDA.Height = 50;
            inkDA.Color = Colors.Purple;
            renderer.DrawingAttributes = inkDA;
            this.StylusPlugIns.Add(renderer);
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
            Console.WriteLine("ASD: " + strokesList.Count);
        }

        private void surfaceDessin_MouseMove(object sender, MouseEventArgs e)
        {
            //if (IsMouseCaptured)
            //{
            //    Console.WriteLine("Captured");
            //    Point p = e.GetPosition(surfaceDessin);
            //    spc.Add(new StylusPoint(p.X, p.Y));

            //    if (spc.Count >= SIZE)
            //    {
            //        Console.WriteLine("More Than " + SIZE);
            //        Stroke s = new Stroke(spc);
            //        PathGeometry pg = s.GetGeometry().GetOutlinedPathGeometry();
            //        socket.Emit("draw", "General", pg.ToString());
            //    }

            //}

            //surfaceDessin.Strokes.StrokesChanged += StrokeColl;
            //surfaceDessin.SourceUpdated += DrawStrokeEventHandler;
        }

        private void DrawStrokeEventHandler(object sender, DataTransferEventArgs e)
        {
            Console.WriteLine("PLZ");
        }

        private void MessageBoxControl_Loaded(object sender, System.Windows.RoutedEventArgs e)
        {

        }

        private void TimerControl_Loaded(object sender, RoutedEventArgs e)
        {

        }

        private void surfaceDessin_StrokeCollected(object sender, InkCanvasStrokeCollectedEventArgs e)
        {
            //Console.WriteLine("PLZ: " + e.Stroke.GetGeometry());
        }

        private void surfaceDessin_StylusDown(object sender, StylusDownEventArgs e)
        {
            Console.WriteLine("Points: " + e.Device);
        }

        private void surfaceDessin_MouseDown(object sender, MouseButtonEventArgs e)
        {
            Console.WriteLine("Capture");
        }

        private void surfaceDessin_ItemAdded(object sender, Xceed.Wpf.Toolkit.ItemEventArgs e)
        {
            Console.WriteLine("DNjSKFNSJK");
        }

        private void surfaceDessin_StylusButtonDown(object sender, StylusButtonEventArgs e)
        {
            Console.WriteLine("DOWN");
        }

        private void surfaceDessin_StylusEnter(object sender, StylusEventArgs e)
        {
            Console.WriteLine("ENter");
        }

        private void surfaceDessin_GotMouseCapture(object sender, MouseEventArgs e)
        {
            Console.WriteLine("MouseCaptutre: ");
        }

        private void surfaceDessin_GotStylusCapture(object sender, StylusEventArgs e)
        {
            Console.WriteLine("StylusCaptutre");
        }

        private void surfaceDessin_LayoutUpdated(object sender, EventArgs e)
        {
            Console.WriteLine("LAyoutUpdated");
        }

        private void surfaceDessin_Gesture(object sender, InkCanvasGestureEventArgs e)
        {
            Console.WriteLine("GESTURE");
        }

        private void surfaceDessin_StrokeErased(object sender, RoutedEventArgs e)
        {
            Console.WriteLine("Stroke Erased");
        }
    }
}
