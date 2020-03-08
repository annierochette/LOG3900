using PolyPaint.Utilitaires;
using System;
using System.Windows.Input;


namespace PolyPaint.VueModeles
{
   public class GameMenuViewModel : BaseViewModel, IPageViewModel { 
   private ICommand _goToUserProfile;
   private ICommand _goToGameModeMenu;
   private ICommand _goToGameCreator;

        public ICommand GoToUserProfile
        {
            get
            {
                return _goToUserProfile ?? (_goToUserProfile = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToUserProfile", "");
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
        public ICommand GoToGameCreator
        {
            get
            {
                return _goToGameCreator ?? (_goToGameCreator = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToGameCreator", "");
                }));
            }
        }
    }
}