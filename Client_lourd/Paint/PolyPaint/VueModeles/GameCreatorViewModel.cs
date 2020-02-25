using PolyPaint.Utilitaires;
using System.Windows.Input;

namespace PolyPaint.VueModeles
{
    class GameCreatorViewModel : BaseViewModel, IPageViewModel
    {
        private ICommand _goToGameModeMenu;

        public ICommand GoToDrawingWindow
        {
            get
            {
                return _goToGameModeMenu ?? (_goToGameModeMenu = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToDrawingWindow", "");
                }));
            }
        }
    }
}
