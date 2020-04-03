using PolyPaint.Utilitaires;
using System;
using System.Windows.Input;


namespace PolyPaint.VueModeles
{
   public class GameMenuViewModel : BaseViewModel, IPageViewModel { 
        private ICommand _goToUserProfile;
        private ICommand _goToGameModeMenu;
        private ICommand _goToGameCreatorControl;

        public override string GetCurrentViewModelName()
        {
            return "GetMenuViewModel";
        }

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
        public ICommand GoToGameCreatorControl
        {
            get
            {
                return _goToGameCreatorControl ?? (_goToGameCreatorControl = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToGameCreatorControl", "");
                }));
            }
        }

    }
}