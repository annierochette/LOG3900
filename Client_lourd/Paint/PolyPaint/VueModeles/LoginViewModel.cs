using PolyPaint.Modeles;
using PolyPaint.Utilitaires;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Input;

namespace PolyPaint.VueModeles
{

    public class LoginControlViewModel : BaseViewModel, IPageViewModel
    {

        public override string GetCurrentViewModelName()
        {
            return "LoginControlViewModel";
        }

        private IPageViewModel _currentLoginViewModel;
        private List<IPageViewModel> _pageViewModels;

        private ICommand _goToLoginWindow;
        private ICommand _goToSignInWindow;
        private ICommand _goToMainWindow;

        public LoginControlViewModel()
        {
            PageViewModels.Add(new LoginViewModel());
            PageViewModels.Add(new SignInViewModel());
            PageViewModels.Add(new MainWindowViewModel());


            CurrentLoginViewModel = PageViewModels[0];

            Mediator.Subscribe("GoToLoginWindow", OnGoToLoginWindow);
            Mediator.Subscribe("GoToSignInWindow", OnGoToSignUpWindow);
            Mediator.Subscribe("GoToMainWindow", OnGoToMainWindow);
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

        public IPageViewModel CurrentLoginViewModel
        {
            get
            {
                return _currentLoginViewModel;
            }
            set
            {
                _currentLoginViewModel = value;
                OnPropertyChanged("CurrentLoginViewModel");
            }
        }

        private void ChangeViewModel(IPageViewModel viewModel)
        {
            if (!PageViewModels.Contains(viewModel))
                PageViewModels.Add(viewModel);

            CurrentLoginViewModel = PageViewModels
                .FirstOrDefault(vm => vm == viewModel);
        }

        private void OnGoToLoginWindow(object obj)
        {
            ChangeViewModel(PageViewModels[0]);
        }

        private void OnGoToSignUpWindow(object obj)
        {
            ChangeViewModel(PageViewModels[1]);
        }

        private void OnGoToMainWindow(object obj)
        {
            ChangeViewModel(PageViewModels[2]);
        }


        public ICommand GoToLoginWindow
        {
            get
            {
                return _goToLoginWindow ?? (_goToLoginWindow = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToLoginWindow", "");
                }));
            }

        }

        public ICommand GoToSignUpWindow
        {
            get
            {
                return _goToSignInWindow ?? (_goToSignInWindow = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToSignInWindow", "");
                }));
            }

        }


        public ICommand GoToMainWindow
        {
            get
            {
                return _goToMainWindow ?? (_goToMainWindow = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToMainWindow", "");
                }));
            }


        }

    }


    public class SignInViewModel : BaseViewModel, IPageViewModel
    {

        public override string GetCurrentViewModelName()
        {
            return "SignInViewModel";
        }

        private ICommand _goToLoginWindow;

        public ICommand GoToLoginWindow
        {
            get
            {
                return _goToLoginWindow ?? (_goToLoginWindow = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToLoginWindow", "");
                }));
            }
        }

    }


    public class LoginViewModel : BaseViewModel, IPageViewModel
    {
        private User user = new User();
        public string Username
        {
            
            get { return user.Username; }
            set { user.Username = value; }
        }

        public override string GetCurrentViewModelName()
        {
            return "LoginViewModel";
        }

        private ICommand _goToMainWindow;
        private ICommand _goToSignInWindow;

        public ICommand GoToMainWindow
        {
            get
            {
                return _goToMainWindow ?? (_goToMainWindow = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToMainWindow", "");
                }));
            }


        }

        public ICommand GoToSignInWindow
        {
            get
            {
                return _goToSignInWindow ?? (_goToSignInWindow = new RelayCommand(x =>
                {
                    Mediator.Notify("GoToSignInWindow", "");
                }));
            }


        }


        public void GiveAccess()
        {
            Mediator.Notify("GoToMainWindow", "");
        }
    }


}
