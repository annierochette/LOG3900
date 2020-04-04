using PolyPaint.Utilitaires;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;

namespace PolyPaint.VueModeles
{
    public class GameChoiceViewModel : BaseViewModel, IPageViewModel
    {

        public override string GetCurrentViewModelName()
        {
            return "GameChoiceViewModel";
        }


        private ICommand _goToWaitingRoom;
        private ICommand _goToFreeForAll;
        private ICommand _goToGameModeMenu;

        public ICommand GoToWaitingRoom
        {
            get
            {
                return _goToWaitingRoom ?? (_goToWaitingRoom = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToWaitingRoom", "");
                }));
            }
        }


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
    }
}

