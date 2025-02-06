package com.example.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.model.ImageItem;
import com.example.myapplication.repository.ImageRepository;
import com.example.myapplication.utils.Constants;
import java.util.List;

public class MainViewModel extends ViewModel {
    private final ImageRepository repository;
    private final MutableLiveData<List<ImageItem>> images = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MainViewModel() {
        repository = new ImageRepository();
    }

    public LiveData<List<ImageItem>> getImages() {
        return images;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadImages() {
        isLoading.setValue(true);
        repository.loadImages(
                Constants.PAGE_NUMBER,
                Constants.LIST_COUNT,
                Constants.LIST_OFFSET,
                new ImageRepository.ImageLoadCallback() {
                    @Override
                    public void onSuccess(List<ImageItem> imageItems) {
                        images.postValue(imageItems);
                        isLoading.postValue(false);
                    }

                    @Override
                    public void onError(Exception e) {
                        isLoading.postValue(false);
                    }
                });
    }
}
