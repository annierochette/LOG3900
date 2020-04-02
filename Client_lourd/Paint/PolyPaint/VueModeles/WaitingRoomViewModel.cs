using PolyPaint.Utilitaires;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;

namespace PolyPaint.VueModeles
{
    public class WaitingRoomViewModel : BaseViewModel, IPageViewModel
    {
        private ICommand _goToGameMenu;
        private ICommand _goToSignInWindow;
        private ICommand _goToGameChoice;

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

        public ICommand GoToGameChoice
        {
            get
            {
                return _goToGameChoice ?? (_goToGameChoice = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToGameChoice", "");
                }));
            }
        }

    }
}