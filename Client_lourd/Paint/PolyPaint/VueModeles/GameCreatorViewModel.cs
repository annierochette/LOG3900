using PolyPaint.Utilitaires;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Input;

namespace PolyPaint.VueModeles
{

    class GameCreatorControlViewModel : BaseViewModel, IPageViewModel
    {
        
        private IPageViewModel _currentCreatorViewModel;
        private List<IPageViewModel> _pageViewModels;

      
    
        public GameCreatorControlViewModel()
        {
            PageViewModels.Add(new GameCreatorViewModel());
            PageViewModels.Add(new NewDrawingViewModel());
            PageViewModels.Add(new ImageImportViewModel());

            CurrentCreatorViewModel = PageViewModels[0];
            
            Mediator.Subscribe("GoToGameCreator", OnGoToGameCreator);
            Mediator.Subscribe("GoToNewDrawingWindow", OnGoToNewDrawingWindow);
            Mediator.Subscribe("GoToImageImport", OnGoToImageImport);
         
        }

        public List<IPageViewModel> PageViewModels
        {
            get
            {
                if (_pageViewModels == null)
                    _pageViewModels = new List<IPageViewModel>();

                return _pageViewModels;
            }
        }

        public IPageViewModel CurrentCreatorViewModel
        {
            get
            {
                return _currentCreatorViewModel;
            }
            set
            {
                _currentCreatorViewModel = value;
                OnPropertyChanged("CurrentCreatorViewModel");
            }
        }

        private void ChangeViewModel(IPageViewModel viewModel)
        {
            if (!PageViewModels.Contains(viewModel))
                PageViewModels.Add(viewModel);

            CurrentCreatorViewModel = PageViewModels
                .FirstOrDefault(vm => vm == viewModel);
        }

        private void OnGoToGameCreator(object obj)
        {
            ChangeViewModel(PageViewModels[0]);
        }

        private void OnGoToNewDrawingWindow(object obj)
        {
            ChangeViewModel(PageViewModels[1]);
        }

        private void OnGoToImageImport(object obj)
        {
            ChangeViewModel(PageViewModels[2]);
        }

      

     

        

    }



    class GameCreatorViewModel : BaseViewModel, IPageViewModel
    {
        private ICommand _goToGameMenu;
        private ICommand _goToGameCreator;
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