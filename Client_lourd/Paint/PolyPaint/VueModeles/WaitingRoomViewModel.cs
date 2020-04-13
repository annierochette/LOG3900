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
        private ICommand _goToFreeForAll;
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

        public ICommand GoToFreeForAll
        {
            get
            {
                return _goToFreeForAll ?? (_goToFreeForAll = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToFreeForAll", "");
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
                Console.WriteLine(data);
                
                TextBoxData = data.ToString();

            });
        }


    }
}