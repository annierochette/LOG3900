using PolyPaint.Utilitaires;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using System.Collections.ObjectModel;

namespace PolyPaint.VueModeles
{
    public class WaitingRoomViewModel : BaseViewModel, IPageViewModel
    {
        private ICommand _goToGameMenu;
        private ICommand _goToSignInWindow;
        public string _textBoxData;
        AppSocket socket = AppSocket.Instance;
        public ObservableCollection<string> SomeCollection { get; set; }
        public ICommand TestCommand { get; private set; }

        public override string GetCurrentViewModelName()
        {
            return "WaitingRoomViewModel";
        }

        public void assignGuessingView()
        {
            Mediator.Notify("GoToGuessingView", "");
        }

        public void assignDrawingView()
        {
            Mediator.Notify("GoToFreeForAll", "");
        }

        public ICommand GoToGameMenu
        {
            get
            {
                return _goToGameMenu ?? (_goToGameMenu = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToGameMenu", "");
                }));
            }


        }

        public ICommand GoToSignInWindow
        {
            get
            {
                return _goToSignInWindow ?? (_goToSignInWindow = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToSignInWindow", "");
                }));
            }


        }

        public string TextBoxData
        {
            get
            {
                return _textBoxData;
            }
            set
            {
                _textBoxData = value;
               
            }
        }

        public WaitingRoomViewModel()
        {

            socket.On("joinGame", (data) =>
            {
                Newtonsoft.Json.Linq.JObject obj = (Newtonsoft.Json.Linq.JObject)data;
                Newtonsoft.Json.Linq.JToken un = obj.GetValue("nbPlayers");

                string test = (string)un;
                //Console.WriteLine("le nombre: " + test);
                Console.WriteLine(test);
                TextBoxData = test;

            });
        }


    }
}