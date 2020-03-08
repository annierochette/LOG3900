using PolyPaint.Utilitaires;
using System;
using System.Collections.Generic;
using System.Linq;

namespace PolyPaint.VueModeles
{
    public class MainWindowViewModel : BaseViewModel
    {
        private IPageViewModel _currentPageViewModel;
        private List<IPageViewModel> _pageViewModels;

        public List<IPageViewModel> PageViewModels
        {
            get
            {
                if (_pageViewModels == null)
                    _pageViewModels = new List<IPageViewModel>();

                return _pageViewModels;
            }
        }

        public IPageViewModel CurrentPageViewModel
        {
            get
            {
                return _currentPageViewModel;
            }
            set
            {
                _currentPageViewModel = value;
                OnPropertyChanged("CurrentPageViewModel");
            }
        }

        private void ChangeViewModel(IPageViewModel viewModel)
        {
            if (!PageViewModels.Contains(viewModel))
                PageViewModels.Add(viewModel);

            CurrentPageViewModel = PageViewModels
                .FirstOrDefault(vm => vm == viewModel);
        }

        private void OnGoToGameMenu(object obj)
        {
            ChangeViewModel(PageViewModels[0]);
        }


        private void OnGoToLoginWindow(object obj)
        {
            ChangeViewModel(PageViewModels[1]);
        }


        private void OnGoToUserProfile(object obj)
        {
            ChangeViewModel(PageViewModels[2]);
        }

        private void OnGoToGameModeMenu(object obj)
        {
            ChangeViewModel(PageViewModels[3]);
        }

        private void OnGoToNewDrawingWindow(object obj)
        {
            ChangeViewModel(PageViewModels[4]);
        }

        private void OnGoToGuessingView(object obj)

        {
            ChangeViewModel(PageViewModels[5]);
        }

        private void OnGoToSignInWindow(object obj)
        {
            ChangeViewModel(PageViewModels[6]);
        }

        private void OnGoToGameCreator(object obj)
        {
            ChangeViewModel(PageViewModels[7]);
        }


        public MainWindowViewModel()
        {
            
            // Add available pages and set page
            PageViewModels.Add(new GameMenuViewModel());
            PageViewModels.Add(new LoginViewModel());
            PageViewModels.Add(new UserProfileViewModel());
            PageViewModels.Add(new GameModeMenuViewModel());
            PageViewModels.Add(new NewDrawingViewModel());
            PageViewModels.Add(new GuessingViewModel());
            PageViewModels.Add(new SignInViewModel());
            PageViewModels.Add(new GameCreatorViewModel());
      

            CurrentPageViewModel = PageViewModels[1];

            Mediator.Subscribe("GoToGameMenu", OnGoToGameMenu);
            Mediator.Subscribe("GoToUserProfile", OnGoToUserProfile);
            Mediator.Subscribe("GoToLoginWindow", OnGoToLoginWindow);
            Mediator.Subscribe("GoToSignInWindow", OnGoToSignInWindow);
            Mediator.Subscribe("GoToGameModeMenu", OnGoToGameModeMenu);
            Mediator.Subscribe("GoToNewDrawingWindow", OnGoToNewDrawingWindow);
            Mediator.Subscribe("GoToGuessingView", OnGoToGuessingView);
            Mediator.Subscribe("GoToGameCreator", OnGoToGameCreator);
           
        }
    }
}
