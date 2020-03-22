using PolyPaint.Utilitaires;
using System.Windows.Input;

namespace PolyPaint.VueModeles
{
    class GameCreatorViewModel : BaseViewModel, IPageViewModel
    {

        //private ICommand _goToGameModeMenu;
        private ICommand _goToDrawingCreator;

        public ICommand GoToDrawingCreator
        {
            get
            {
                return _goToDrawingCreator ?? (_goToDrawingCreator = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToDrawingCreator", "");
                }));
            }
        }
    }

    class DrawingCreatorViewModel : DrawingWindowViewModel
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