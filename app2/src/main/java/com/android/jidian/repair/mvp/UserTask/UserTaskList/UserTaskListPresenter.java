package com.android.jidian.repair.mvp.UserTask.UserTaskList;

import com.android.jidian.repair.base.BasePresenter;
import com.android.jidian.repair.net.RxScheduler;

/**
 * @author : xiaoming
 * date: 2023/2/3 15:37
 * description:
 */
public class UserTaskListPresenter extends BasePresenter<UserTaskListContract.View> implements UserTaskListContract.Presenter {

    private UserTaskListContract.Model mModel;

    public UserTaskListPresenter() {
        mModel = new UserTaskListModel();
    }

    @Override
    public void requestWorktaskMylists(String page) {
        if (!isViewAttached()) {
            return;
        }
        if (mView != null) {
            mView.showProgress();
        }
        mModel.requestWorktaskMylists(page)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(bean -> {
                    if (mView != null) {
                        mView.hideProgress();
                        if (bean != null) {
                            if ("1".equals(bean.getStatus())) {
                                mView.requestWorktaskMylistsSuccess(bean);
                            } else {
                                mView.requestShowTips(bean.getMsg());
                            }
                        }
                    }
                }, throwable -> {
                    if (mView != null) {
                        mView.hideProgress();
                        mView.onError(throwable);
                    }
                });
    }
}
