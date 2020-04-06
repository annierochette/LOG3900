using PolyPaint.Utilitaires;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PolyPaint.VueModeles
{
    public class MainWindowViewModel : BaseViewModel, IPageViewModel
    {
        private IPageViewModel _currentPageViewModel;
        private List<IPageViewModel> _pageViewModels;

        public override string GetCurrentViewModelName()
        {
            return "MainWindowViewModel";
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

        public void ChangeViewModel(IPageViewModel viewModel)
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

        private void OnGoToGameCreatorControl(object obj)
        {
            ChangeViewModel(PageViewModels[7]);
        }

        private void OnGoToFreeForAll(object obj)
        {
            ChangeViewModel(PageViewModels[8]);
        }

        private void OnGoToGameChoice(object obj)
        {
            ChangeViewModel(PageViewModels[9]);
        }

        private void OnGoToWaitingRoom(object obj) {
            ChangeViewModel(PageViewModels[10]);
        }

        private void OnGoToJoiningGame(object obj)
        {
            ChangeViewModel(PageViewModels[11]);
        }

        public MainWindowViewModel()
        {
           
            PageViewModels.Add(new GameMenuViewModel());      //0
            PageViewModels.Add(new LoginViewModel());         //1
            PageViewModels.Add(new UserProfileViewModel());   //2
            PageViewModels.Add(new GameModeMenuViewModel());  //3
            PageViewModels.Add(new DrawingWindowViewModel()); //4
            PageViewModels.Add(new GuessingViewModel());      //5
            PageViewModels.Add(new SignInViewModel());        //6
            PageViewModels.Add(new GameCreatorControlViewModel());   //7
            PageViewModels.Add(new FreeForAllViewModel());    //8
            PageViewModels.Add(new GameChoiceViewModel());    //9
            PageViewModels.Add(new WaitingRoomViewModel());   //10
            PageViewModels.Add(new JoiningGameViewModel());   //11
                  

            CurrentPageViewModel = PageViewModels[0];

            Mediator.Subscribe("GoToGameMenu", OnGoToGameMenu);
            Mediator.Subscribe("GoToUserProfile", OnGoToUserProfile);
            Mediator.Subscribe("GoToLogin", OnGoToLoginWindow);
            Mediator.Subscribe("GoToSignIn", OnGoToSignInWindow);
            Mediator.Subscribe("GoToGameModeMenu", OnGoToGameModeMenu);
            Mediator.Subscribe("GoToDrawingWindow", OnGoToDrawingWindow);
            Mediator.Subscribe("GoToGuessingView", OnGoToGuessingView);
            Mediator.Subscribe("GoToGameCreatorControl", OnGoToGameCreatorControl); ;
            Mediator.Subscribe("GoToFreeForAll", OnGoToFreeForAll);
            Mediator.Subscribe("GoToGameChoice", OnGoToGameChoice);
            Mediator.Subscribe("GoToWaitingRoom", OnGoToWaitingRoom);
            Mediator.Subscribe("GoToJoiningGame", OnGoToJoiningGame);
      
        }
    }
    
}
