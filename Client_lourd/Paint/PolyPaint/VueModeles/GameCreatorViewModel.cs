using PolyPaint.Modeles;
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

        public override string GetCurrentViewModelName()
        {
            return "GameCreatorControlViewModel";
        }

        public GameCreatorControlViewModel()
        {
            PageViewModels.Add(new GameCreatorViewModel());
            PageViewModels.Add(new NewDrawingViewModel());
            PageViewModels.Add(new ImageImportViewModel());
            PageViewModels.Add(new NewGameFormViewModel());

            CurrentCreatorViewModel = PageViewModels[0];

            Mediator.Subscribe("GoToGameCreator", OnGoToGameCreator);
            Mediator.Subscribe("GoToNewDrawingWindow", OnGoToNewDrawingWindow);
            Mediator.Subscribe("GoToImageImport", OnGoToImageImport);
            Mediator.Subscribe("GoToNewGameForm", OnGoToNewGameForm);
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
        private void OnGoToNewGameForm(object obj)
        {
            ChangeViewModel(PageViewModels[3]);
        }

    }



    class GameCreatorViewModel : BaseViewModel, IPageViewModel
    {
        private ICommand _goToGameMenu;
        private ICommand _goToGameCreator;
        private ICommand _goToNewDrawingWindow;
        private ICommand _goToImageImport;
        private ICommand _goToQuickdrawSuggestion;

        public override string GetCurrentViewModelName()
        {
            return "GameCreatorViewModel";
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
        private ICommand _goToNewGameForm;

        public override string GetCurrentViewModelName()
        {
            return "ImageImportViewModel";
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

        public ICommand GoToNewGameForm
        {
            get
            {
                return _goToNewGameForm ?? (_goToNewGameForm = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToNewGameForm", "");
                }));
            }
        }
    }

    class NewGameFormViewModel : BaseViewModel, IPageViewModel
    {
        private ICommand _goToImageImport;
        private ICommand _goToGameCreator;

        public override string GetCurrentViewModelName()
        {
            return "NewGameFormViewModel";
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

}