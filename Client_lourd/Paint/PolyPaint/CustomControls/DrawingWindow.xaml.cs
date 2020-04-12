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
        private string OutilSelectionne = Tool.PEN;

        public DrawingWindow()
        {
            InitializeComponent();

            drawingAttributes = surfaceDessin.DefaultDrawingAttributes;
            drawingAttributes.Color = (Color)ColorConverter.ConvertFromString("#000000");
            drawingAttributes.Height = 10;
            drawingAttributes.Width = 10;
            drawingAttributes.StylusTip = StylusTip.Ellipse;
            
            socket.On(SocketEvents.STROKE_DRAWING, (points) => {
                Dispatcher.Invoke(() => {

                    Stroke newStroke = new Stroke(JsonConvert.DeserializeObject<StylusPointCollection>(points.ToString()));
                    newStroke.DrawingAttributes = drawingAttributes;
                    newStroke.DrawingAttributes.Color = drawingAttributes.Color;
                    newStroke.DrawingAttributes.Width = drawingAttributes.Width;
                    newStroke.DrawingAttributes.Height = drawingAttributes.Height;
                    newStroke.DrawingAttributes.StylusTip = drawingAttributes.StylusTip;
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
                    newStroke.DrawingAttributes.StylusTip = drawingAttributes.StylusTip;
                    
                    foreach (Stroke strokeToDelete in ongoingStrokeIndex)
                    {
                        surfaceDessin.Strokes.Remove(strokeToDelete);
                    }

                    surfaceDessin.Strokes.Add(newStroke);
                    ongoingStrokeIndex.Clear();

                });
            });

            socket.On(SocketEvents.STROKE_ERASING, (points) => {
                Dispatcher.Invoke(() => {
                    List<StylusPoint> pointsToCheck = (JsonConvert.DeserializeObject<List<StylusPoint>>(points.ToString()));
                    pointsToCheck.ForEach((point) => {
                        StrokeCollection strokesToBeErased = surfaceDessin.Strokes.HitTest(new Point(point.X, point.Y));
                        surfaceDessin.Strokes.Remove(strokesToBeErased);
                    });
                });
            });

            socket.On(SocketEvents.STROKE_SEGMENT_ERASING, (points) => {
                Dispatcher.Invoke(() => {
                    List<StylusPoint> pointsToCheck = (JsonConvert.DeserializeObject<List<StylusPoint>>(points.ToString()));
                    
                    Console.WriteLine(pointsToCheck.Count + " POINTS");
                    IncrementalStrokeHitTester eraserTester = surfaceDessin.Strokes.GetIncrementalStrokeHitTester(generateStylusShape());
                    eraserTester.StrokeHit += EraserTester_StrokeHit;
                    pointsToCheck.ForEach((point) => eraserTester.AddPoint(new Point(point.X, point.Y)));
                    eraserTester.StrokeHit -= EraserTester_StrokeHit;
                    eraserTester.EndHitTesting();
                });
            });

            socket.On(SocketEvents.STROKE_COLOR, (color) =>
            {
                Dispatcher.Invoke(() => {
                    string hex = "#" + ((Int64) color).ToString("X8");
                    drawingAttributes.Color = (Color)ColorConverter.ConvertFromString(hex);
                });
            });

            socket.On(SocketEvents.STROKE_SIZE, (size) =>
            {
                Dispatcher.Invoke(() => {
                    drawingAttributes.Height = Convert.ToInt32(size.ToString());
                    drawingAttributes.Width = Convert.ToInt32(size.ToString());
                });
            });

            socket.On(SocketEvents.STROKE_TIP, (tip) =>
            {
                Dispatcher.Invoke(() => {
                    drawingAttributes.StylusTip = ((string) tip == Tool.PEN_ROUND_TIP) ? StylusTip.Ellipse : StylusTip.Rectangle;
                });
            });

            socket.On(SocketEvents.STROKE_TOOL, (tool) =>
            {
                Dispatcher.Invoke(() => {
                    OutilSelectionne = tool.ToString();
                });
            });
        }

        private void EraserTester_StrokeHit(object sender, StrokeHitEventArgs e)
        {
            StrokeCollection eraseResult = e.GetPointEraseResults();
            Console.WriteLine("Strokes eraseResult: " + eraseResult.Count);
            StrokeCollection strokesToReplace = new StrokeCollection();
            strokesToReplace.Add(e.HitStroke);

            // Replace the old stroke with the new one.
            if (eraseResult.Count > 0)
            {
                Console.WriteLine("LETS REPLACE");
                surfaceDessin.Strokes.Replace(strokesToReplace, eraseResult);
            }

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
                if (OutilSelectionne == Tool.PEN)
                {
                    socket.Emit(SocketEvents.STROKE_DRAWING, MATCH_CHANNEL, JsonConvert.SerializeObject(pointsBucket, new StylusPointConverter()));
                }
                else if (OutilSelectionne == Tool.SEGMENT_ERASER)
                {
                    socket.Emit(SocketEvents.STROKE_SEGMENT_ERASING, MATCH_CHANNEL, JsonConvert.SerializeObject(pointsBucket, new StylusPointConverter()));
                }
                else
                {
                    socket.Emit(SocketEvents.STROKE_ERASING, MATCH_CHANNEL, JsonConvert.SerializeObject(pointsBucket, new StylusPointConverter()));
                }

                pointsBucket.Clear();
            }
        }

        private void surfaceDessin_StrokeCollected(object sender, InkCanvasStrokeCollectedEventArgs e)
        {
            Console.WriteLine("StrokeCollected");
            pointsBucket.Clear();
            socket.Emit(SocketEvents.STROKE_COLLECTED, MATCH_CHANNEL, JsonConvert.SerializeObject(e.Stroke.StylusPoints, new StylusPointConverter()));
        }

        private void surfaceDessin_StrokeErased(object sender, RoutedEventArgs e)
        {
            if (OutilSelectionne == Tool.STROKE_ERASER)
            {
                socket.Emit(SocketEvents.STROKE_ERASING, MATCH_CHANNEL, JsonConvert.SerializeObject(pointsBucket, new StylusPointConverter()));
            }
            else if (OutilSelectionne == Tool.SEGMENT_ERASER)
            {
                socket.Emit(SocketEvents.STROKE_SEGMENT_ERASING, MATCH_CHANNEL, JsonConvert.SerializeObject(pointsBucket, new StylusPointConverter()));
            }

            pointsBucket.Clear();
        }

        private StylusShape generateStylusShape()
        {
            double size = drawingAttributes.Width;
            StylusShape stylusShape;
            
            if (drawingAttributes.StylusTip == StylusTip.Ellipse)
            {
                stylusShape = new EllipseStylusShape(size, size);
            }
            else
            {
                stylusShape = new RectangleStylusShape(size, size);
            }

            return stylusShape;
        }
    }
}
