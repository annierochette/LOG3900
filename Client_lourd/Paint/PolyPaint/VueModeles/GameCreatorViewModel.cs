using PolyPaint.Utilitaires;
using System.Windows.Input;

namespace PolyPaint.VueModeles
{
    class GameCreatorViewModel : BaseViewModel, IPageViewModel
    {

        private ICommand _goToGameModeMenu;


        public ICommand GoToNewDrawingWindow
        {
            get
            {
                return _goToGameModeMenu ?? (_goToGameModeMenu = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToNewDrawingWindow", "");
                }));
            }
        }
    }

    class NewDrawingViewModel : DrawingWindowViewModel
    {
        private ICommand _goToGameCreator;


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