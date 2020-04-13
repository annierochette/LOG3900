﻿using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using PolyPaint.Modeles;
using PolyPaint.Utilitaires;
using Svg;
using System.Windows.Markup;
using System.Xml.Linq;
using System;
using Newtonsoft.Json;
using System.Web.Script.Serialization;
using PolyPaint.Vues;

namespace PolyPaint.VueModeles
{

    /// <summary>
    /// Sert d'intermédiaire entre la vue et le modèle.
    /// Expose des commandes et propriétés connectées au modèle aux des éléments de la vue peuvent se lier.
    /// Reçoit des avis de changement du modèle et envoie des avis de changements à la vue.
    /// </summary>
    class FreeForAllViewModel :  INotifyPropertyChanged, IPageViewModel
    {
        response info;
        public string _textBoxWordData;

        public string TextBoxWordData
        {
            get
            {
                return _textBoxWordData;
            }
            set
            {
                _textBoxWordData = value;

            }
        }
        public class game
        {

            [JsonProperty("clues")]
            public string[] clues { get; set; }
            
            [JsonProperty("_id")]
            public string _id { get; set; }

            [JsonProperty("name")]
            public string name { get; set; }

            [JsonProperty("__v")]
            public int __v { get; set; }

        }

        public class response
        {

            [JsonProperty("draftsman")]
            public string draftsman { get; set; }

            [JsonProperty("game")]
            public game game { get; set; }

            [JsonProperty("status")]
            public string Status { get; set; }



        }

        public event PropertyChangedEventHandler PropertyChanged;
        private Editeur editeur = new Editeur();
        private SvgDocument newImage = new SvgDocument();
        private AppSocket socket = AppSocket.Instance;

        // Ensemble d'attributs qui définissent l'apparence d'un trait.
        public DrawingAttributes AttributsDessin { get; set; } = new DrawingAttributes();
        private bool _drawing;
        private bool _guessing;
       
        

        public bool ActivateDrawing
        {
            get { return _drawing; }
            private set
            {
                _drawing = value;

            }
        }

        public bool ActivateGuessing
        {
            get { return _guessing; }
            private set
            {
                _guessing = value;

            }
        }

        public void assignGuessingView()
        {
            Mediator.Notify("GoToGuessingView", "");
        }

        private ICommand _goToGameModeMenu;

        public ICommand GoToGameModeMenu
        {
            get
            {
                return _goToGameModeMenu ?? (_goToGameModeMenu = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToGameModeMenu", "");
                }));
            }
        }

        public ICommand ButtonCommand { get; set; }


        public string OutilSelectionne
        {
            get { return editeur.OutilSelectionne; }
            set { ProprieteModifiee(); }
        }

        public string CouleurSelectionnee
        {
            get { return editeur.CouleurSelectionnee; }
            set { editeur.CouleurSelectionnee = value; }
        }

        public string PointeSelectionnee
        {
            get { return editeur.PointeSelectionnee; }
            set { ProprieteModifiee(); }
        }

        public int TailleTrait
        {
            get { return editeur.TailleTrait; }
            set { editeur.TailleTrait = value; }
        }

        public StrokeCollection Traits { get; set; }

        // Commandes sur lesquels la vue pourra se connecter.
        public RelayCommand<string> ChoisirPointe { get; set; }
        public RelayCommand<string> ChoisirOutil { get; set; }

        /// <summary>
        /// Constructeur de VueModele
        /// On récupère certaines données initiales du modèle et on construit les commandes
        /// sur lesquelles la vue se connectera.
        /// </summary>
        public FreeForAllViewModel()
        {

            JoiningGameWindow tes = new JoiningGameWindow();
            socket.On("nextRound", (data) =>
            {
                Console.WriteLine("se rend au next round");
            //    string test = data.ToString();
                Console.WriteLine(data);
                JavaScriptSerializer js = new JavaScriptSerializer();
                info  = js.Deserialize<response>(data.ToString());
                string name = info.draftsman;
                string user = User.instance.Username;
                if (name == user)
                {
                    ActivateDrawing = true;
                    ActivateGuessing = false;
                }
                else { 
                    ActivateDrawing = false;
                    ActivateGuessing = true;
                }

                
                TextBoxWordData = info.game.name;

            });

            socket.On("answer", (data) =>
            {
                Newtonsoft.Json.Linq.JObject obj = (Newtonsoft.Json.Linq.JObject)data;
                Newtonsoft.Json.Linq.JToken un = obj.GetValue("valid");
                Newtonsoft.Json.Linq.JToken ts = obj.GetValue("score");


            });

            ButtonCommand = new RelayCommand(o => ConvertDrawingToSVG("ToSVG"));
            // On écoute pour des changements sur le modèle. Lorsqu'il y en a, EditeurProprieteModifiee est appelée.
            editeur.PropertyChanged += new PropertyChangedEventHandler(EditeurProprieteModifiee);

            // On initialise les attributs de dessin avec les valeurs de départ du modèle.
            AttributsDessin = new DrawingAttributes();
            AttributsDessin.Color = (Color)ColorConverter.ConvertFromString(editeur.CouleurSelectionnee);
            AjusterPointe();

            Traits = editeur.traits;

            // Pour les commandes suivantes, il est toujours possible des les activer.
            // Donc, aucune vérification de type Peut"Action" à faire.
            ChoisirPointe = new RelayCommand<string>(editeur.ChoisirPointe);
            ChoisirOutil = new RelayCommand<string>(editeur.ChoisirOutil);
        }

        private void ConvertDrawingToSVG(object sender)
        {

            var svg = new SvgDocument();
            var colorServer = new SvgColourServer(System.Drawing.Color.Black);

            var group = new SvgGroup { Fill = colorServer, Stroke = colorServer };
            svg.Children.Add(group);

            foreach (var stroke in Traits)
            {
                var geometry = stroke.GetGeometry(stroke.DrawingAttributes).GetOutlinedPath‌​Geometry();

                var s = XamlWriter.Save(geometry);

                if (!string.IsNullOrEmpty(s))
                {
                    var element = XElement.Parse(s);

                    var data = element.Attribute("Figures")?.Value;

                    if (!string.IsNullOrEmpty(data))
                    {
                        group.Children.Add(new SvgPath
                        {
                            PathData = SvgPathBuilder.Parse(data),
                            Fill = colorServer,
                            Stroke = colorServer
                        });
                    }
                }
            }
            Console.WriteLine(group.GetXML());
            //newImage = svg;
        }


        /// <summary>
        /// Appelee lorsqu'une propriété de VueModele est modifiée.
        /// Un évènement indiquant qu'une propriété a été modifiée est alors émis à partir de VueModèle.
        /// L'évènement qui contient le nom de la propriété modifiée sera attrapé par la vue qui pourra
        /// alors mettre à jour les composants concernés.
        /// </summary>
        /// <param name="propertyName">Nom de la propriété modifiée.</param>
        protected virtual void ProprieteModifiee([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }

        /// <summary>
        /// Traite les évènements de modifications de propriétés qui ont été lancés à partir
        /// du modèle.
        /// </summary>
        /// <param name="sender">L'émetteur de l'évènement (le modèle)</param>
        /// <param name="e">Les paramètres de l'évènement. PropertyName est celui qui nous intéresse. 
        /// Il indique quelle propriété a été modifiée dans le modèle.</param>
        private void EditeurProprieteModifiee(object sender, PropertyChangedEventArgs e)
        {
            if (e.PropertyName == "CouleurSelectionnee")
            {
                socket.Emit(SocketEvents.STROKE_COLOR, "General", Convert.ToInt64(editeur.CouleurSelectionnee.Substring(1), 16));
                AttributsDessin.Color = (Color)ColorConverter.ConvertFromString(editeur.CouleurSelectionnee);
            }
            else if (e.PropertyName == "OutilSelectionne")
            {
                socket.Emit(SocketEvents.STROKE_TOOL, "General", editeur.OutilSelectionne);
                OutilSelectionne = editeur.OutilSelectionne;
            }
            else if (e.PropertyName == "PointeSelectionnee")
            {
                socket.Emit(SocketEvents.STROKE_TIP, "General", editeur.PointeSelectionnee);
                PointeSelectionnee = editeur.PointeSelectionnee;
                AjusterPointe();
            }
            else // e.PropertyName == "TailleTrait"
            {
                socket.Emit(SocketEvents.STROKE_SIZE, "General", editeur.TailleTrait);
                AjusterPointe();
            }
        }

        /// <summary>
        /// C'est ici qu'est défini la forme de la pointe, mais aussi sa taille (TailleTrait).
        /// Pourquoi deux caractéristiques se retrouvent définies dans une même méthode? Parce que pour créer une pointe 
        /// horizontale ou verticale, on utilise une pointe carrée et on joue avec les tailles pour avoir l'effet désiré.
        /// </summary>
        private void AjusterPointe()
        {
            AttributsDessin.StylusTip = (editeur.PointeSelectionnee == "ronde") ? StylusTip.Ellipse : StylusTip.Rectangle;
            AttributsDessin.Width = editeur.TailleTrait;
            AttributsDessin.Height = editeur.TailleTrait;
        }
    }

}
