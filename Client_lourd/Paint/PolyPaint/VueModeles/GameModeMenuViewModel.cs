using PolyPaint.Utilitaires;
using System.Windows.Input;


namespace PolyPaint.VueModeles
{
    public class GameModeMenuViewModel : BaseViewModel, IPageViewModel
    {
        private ICommand _goToDrawingWindow;
        private ICommand _goToGameMenu;
        private ICommand _goToUserProfile;

        public ICommand GoToDrawingWindow
        {
            get
            {
                return _goToDrawingWindow ?? (_goToDrawingWindow = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToDrawingWindow", "");
                }));
            }
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
    }
}