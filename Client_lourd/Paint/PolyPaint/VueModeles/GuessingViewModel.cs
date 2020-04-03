using PolyPaint.Utilitaires;
using System.Windows.Input;

namespace PolyPaint.VueModeles
{
    class GuessingViewModel : BaseViewModel, IPageViewModel
    {
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
    }
}
