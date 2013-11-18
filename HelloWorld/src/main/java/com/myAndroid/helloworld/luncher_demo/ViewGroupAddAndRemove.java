package com.myAndroid.helloworld.luncher_demo;

import android.view.View;

import android.view.ViewGroup;

public class ViewGroupAddAndRemove {
  private ViewGroup viewGroup;
  private View listenerView;
  private View dragView;

  /**
   * @return the dragView
   */
  public View getDragView() {
    return dragView;
  }

  /**
   * @return the listenerView
   */
  public View getListenerView() {
    return listenerView;
  }

  /**
   * @return the viewGroup
   */
  public ViewGroup getViewGroup() {
    return viewGroup;
  }

  /**
   * @param dragView the dragView to set
   */
  public void setDragView(View dragView) {
    this.dragView = dragView;
  }

  /**
   * @param listenerView the listenerView to set
   */
  public void setListenerView(View listenerView) {
    this.listenerView = listenerView;
  }

  /**
   * @param viewGroup the viewGroup to set
   */
  public void setViewGroup(ViewGroup viewGroup) {
    this.viewGroup = viewGroup;
  }

}
