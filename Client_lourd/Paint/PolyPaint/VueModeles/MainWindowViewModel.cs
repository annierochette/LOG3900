using PolyPaint.Utilitaires;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

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
            ChangeViewModel(PageViewModels[2]);
        }


        private void OnGoToUserProfile(object obj)
        {
            ChangeViewModel(PageViewModels[1]);
        }

        private void OnGoToGameModeMenu(object obj)
        {
            ChangeViewModel(PageViewModels[3]);
        }

        private void OnGoToDrawingWindow(object obj)
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

        public MainWindowViewModel()
        {
            
            // Add available pages and set page
            PageViewModels.Add(new GameMenuViewModel());
            PageViewModels.Add(new UserProfileViewModel());
            PageViewModels.Add(new LoginViewModel());
            PageViewModels.Add(new GameModeMenuViewModel());
            PageViewModels.Add(new DrawingWindowViewModel());
            PageViewModels.Add(new GuessingViewModel());
            PageViewModels.Add(new SignInViewModel());

            CurrentPageViewModel = PageViewModels[2];

            Mediator.Subscribe("GoToGameMenu", OnGoToGameMenu);
            Mediator.Subscribe("GoToUserProfile", OnGoToUserProfile);
            Mediator.Subscribe("GoToLoginWindow", OnGoToLoginWindow);
            Mediator.Subscribe("GoToSignInWindow", OnGoToSignInWindow);
            Mediator.Subscribe("GoToGameModeMenu", OnGoToGameModeMenu);
            Mediator.Subscribe("GoToDrawingWindow", OnGoToDrawingWindow);
            Mediator.Subscribe("GoToGuessingView", OnGoToGuessingView);
        }
    }
}
