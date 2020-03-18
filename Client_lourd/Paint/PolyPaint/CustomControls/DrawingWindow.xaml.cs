using System;
using System.Windows;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Controls.Primitives;
using PolyPaint.VueModeles;
using System.Windows.Controls;
using System.Collections.Generic;
using PolyPaint.Utilitaires;
using Newtonsoft.Json;
using System.Windows.Ink;
using PolyPaint.Convertisseurs;

namespace PolyPaint.CustomControls
{
    /// <summary>
    /// Logique d'interaction pour FenetreDessin.xaml
    /// </summary>
    public partial class DrawingWindow : UserControl
    {
        private readonly int BUCKET_SIZE = 25;
        private List<StylusPoint> pointsBucket = new List<StylusPoint>();
        private AppSocket socket = AppSocket.Instance;
        private string MATCH_CHANNEL = "General";
        private StrokeCollection ongoingStrokeIndex = new StrokeCollection();
        private DrawingAttributes drawingAttributes;
        private string OutilSelectionne = "ronde";

        public DrawingWindow()
        {
            InitializeComponent();

            drawingAttributes = surfaceDessin.DefaultDrawingAttributes;
            drawingAttributes.Color = (Color)ColorConverter.ConvertFromString("#000000");
            drawingAttributes.Height = 10;
            drawingAttributes.Width = 10;

            socket.On(SocketEvents.STROKE_DRAWING, (points) => {
                Console.WriteLine("ACtually drawing");
                Dispatcher.Invoke(() => {

                    Stroke newStroke = new Stroke(JsonConvert.DeserializeObject<StylusPointCollection>(points.ToString()));
                    newStroke.DrawingAttributes = drawingAttributes;
                    newStroke.DrawingAttributes.Color = drawingAttributes.Color;
                    newStroke.DrawingAttributes.Width = drawingAttributes.Width;
                    newStroke.DrawingAttributes.Height = drawingAttributes.Height;
                    surfaceDessin.Strokes.Add(newStroke);
                    ongoingStrokeIndex.Add(newStroke);
                });
            });

            socket.On(SocketEvents.STROKE_COLLECTED, (points) => {
                Dispatcher.Invoke(() => {
                    Stroke newStroke = new Stroke(JsonConvert.DeserializeObject<StylusPointCollection>(points.ToString()));
                    newStroke.DrawingAttributes.Color = drawingAttributes.Color;
                    newStroke.DrawingAttributes.Width = drawingAttributes.Width;
                    newStroke.DrawingAttributes.Height = drawingAttributes.Height;

                    surfaceDessin.Strokes.Add(newStroke);
                    
                    foreach (Stroke strokeToDelete in ongoingStrokeIndex)
                    {
                        surfaceDessin.Strokes.Remove(strokeToDelete);
                    }

                    ongoingStrokeIndex.Clear();

                });
            });

            socket.On(SocketEvents.STROKE_COLOR, (color) =>
            {
                Console.WriteLine("Color: " + color.ToString());
                Dispatcher.Invoke(() => { 
                    drawingAttributes.Color = (Color)ColorConverter.ConvertFromString(color.ToString());
                });
            });

            socket.On(SocketEvents.STROKE_SIZE, (size) =>
            {
                Console.WriteLine("Size: " + size.ToString());
                Dispatcher.Invoke(() => {
                    drawingAttributes.Height = Convert.ToInt32(size.ToString());
                    drawingAttributes.Width = Convert.ToInt32(size.ToString());
                });
            });

            socket.On(SocketEvents.STROKE_TIP, (tip) =>
            {
                Console.WriteLine("Tip: " + tip);
                Dispatcher.Invoke(() => {
                    drawingAttributes.StylusTip = ((string) tip == "ronde") ? StylusTip.Ellipse : StylusTip.Rectangle;
                });
            });

            socket.On(SocketEvents.STROKE_TOOL, (tool) =>
            {
                Console.WriteLine("Tool: " + tool.ToString());
                Dispatcher.Invoke(() => {
                    OutilSelectionne = (string) tool;
                });
            });
        }
        
        // Pour gérer les points de contrôles.
        private void GlisserCommence(object sender, DragStartedEventArgs e) => (sender as Thumb).Background = Brushes.Black;
        private void GlisserTermine(object sender, DragCompletedEventArgs e) => (sender as Thumb).Background = Brushes.White;
        private void GlisserMouvementRecu(object sender, DragDeltaEventArgs e)
        {
            String nom = (sender as Thumb).Name;
            if (nom == "horizontal" || nom == "diagonal") colonne.Width = new GridLength(Math.Max(32, colonne.Width.Value + e.HorizontalChange));
            if (nom == "vertical" || nom == "diagonal") ligne.Height = new GridLength(Math.Max(32, ligne.Height.Value + e.VerticalChange));
        }

        private void surfaceDessin_MouseMove(object sender, MouseEventArgs e)
        {
            if (surfaceDessin.IsMouseCaptured)
            {
                Point p = e.GetPosition(surfaceDessin);
                pointsBucket.Add(new StylusPoint(p.X, p.Y));

                if (pointsBucket.Count.Equals(BUCKET_SIZE))
                {
                    uploadPoints();
                }
            }

        }

        private void uploadPoints()
        {
            if (pointsBucket.Count > 0)
            {
                socket.Emit(SocketEvents.STROKE_DRAWING, MATCH_CHANNEL, JsonConvert.SerializeObject(pointsBucket, new StylusPointConverter()));
                pointsBucket.Clear();
            }
        }

        private void surfaceDessin_StrokeCollected(object sender, InkCanvasStrokeCollectedEventArgs e)
        {
            Console.WriteLine("StrokeCollected");
            pointsBucket.Clear();
            socket.Emit(SocketEvents.STROKE_COLLECTED, MATCH_CHANNEL, JsonConvert.SerializeObject(e.Stroke.StylusPoints, new StylusPointConverter()));
        }
    }
}
