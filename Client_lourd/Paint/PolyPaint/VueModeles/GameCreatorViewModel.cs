using PolyPaint.Utilitaires;
using System.Windows.Input;

namespace PolyPaint.VueModeles
{
    class GameCreatorViewModel : BaseViewModel, IPageViewModel
    {
        private ICommand _goToGameMenu;
        private ICommand _goToNewDrawingWindow;
        private ICommand _goToImageImport;

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

        public ICommand GoToNewDrawingWindow
        {
            get
            {
                return _goToNewDrawingWindow ?? (_goToNewDrawingWindow = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToNewDrawingWindow", "");
                }));
            }
        }

        public ICommand GoToImageImport
        {
            get
            {
                return _goToImageImport ?? (_goToImageImport = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToImageImport", "");
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

    class ImageImportViewModel : BaseViewModel, IPageViewModel
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