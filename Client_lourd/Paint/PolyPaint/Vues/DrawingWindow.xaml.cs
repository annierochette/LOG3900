using PolyPaint.VueModeles;
using Quobject.SocketIoClientDotNet.Client;
using System;
using System.Windows;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Controls.Primitives;
using System.Windows.Controls;
using System.Collections.Generic;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Collections.ObjectModel;
using System.Windows.Ink;

namespace PolyPaint.Vues
{
    /// <summary>
    /// Logique d'interaction pour FenetreDessin.xaml
    /// </summary>
    public partial class DrawingWindow : UserControl
    {
        private readonly int BUCKET_SIZE = 25;
        private List<Point> pointBucket = new List<Point>();
        private Socket socket;

        public DrawingWindow()
        {
            InitializeComponent();
            socket = IO.Socket("http://192.168.211.1:5050");

            socket.On("draw", (data) =>
            {
                Newtonsoft.Json.Linq.JObject obj = (Newtonsoft.Json.Linq.JObject)data;
                Newtonsoft.Json.Linq.JToken points = obj.GetValue("points");
                Newtonsoft.Json.Linq.JToken attributs = obj.GetValue("attributs");
                Console.WriteLine("On draw: " + points);
                DrawingAttributes AttributsDessin = JsonConvert.DeserializeObject<DrawingAttributes>(attributs.ToString());
                //((DrawingWindowViewModel)(this.DataContext)).AttributsDessin = AttributsDessin;
                StylusPointCollection scp = JsonConvert.DeserializeObject<StylusPointCollection>(points.ToString());
                Stroke s = new Stroke(scp);
                if (!Dispatcher.CheckAccess())
                {
                    Dispatcher.Invoke(() =>
                    {
                        s.DrawingAttributes =AttributsDessin;
                        surfaceDessin.Strokes.Add(s);
                    });
                }
                else {
                        s.DrawingAttributes = AttributsDessin;
                    surfaceDessin.Strokes.Add(s);
                }

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

        // Pour la gestion de l'affichage de position du pointeur.
        private void surfaceDessin_MouseLeave(object sender, MouseEventArgs e) => textBlockPosition.Text = "";
        private void surfaceDessin_MouseMove(object sender, MouseEventArgs e)
        {
            Point p = e.GetPosition(surfaceDessin);
            textBlockPosition.Text = Math.Round(p.X) + ", " + Math.Round(p.Y) + "px";

            if (surfaceDessin.IsMouseCaptured)
            {
                pointBucket.Add(p);

                if (pointBucket.Count.Equals(BUCKET_SIZE))
                {
                    uploadPoints();
                }
            } else if (pointBucket.Count > 0) 
            {
                uploadPoints();
            }

        }

        private void uploadPoints()
        {
            string points = JsonConvert.SerializeObject(pointBucket, new PointConverter());
            DrawingAttributes attributs = surfaceDessin.DefaultDrawingAttributes.Clone();
            socket.Emit("draw", "General", points, JsonConvert.SerializeObject(attributs));
            pointBucket.Clear();
        }

        private class PointConverter : JsonConverter
        {
            public override void WriteJson(
                JsonWriter writer, object value, JsonSerializer serializer)
            {
                var point = (Point)value;

                serializer.Serialize(
                    writer, new JObject { { "X", point.X }, { "Y", point.Y } });
            }

            public override object ReadJson(
                JsonReader reader, Type objectType, object existingValue,
                JsonSerializer serializer)
            {
                var jObject = serializer.Deserialize<JObject>(reader);

                return new StylusPoint((double)jObject["X"], (double)jObject["Y"]);
            }

            public override bool CanConvert(Type objectType)
            {
                return objectType == typeof(Point);
            }
        }

    }
}
