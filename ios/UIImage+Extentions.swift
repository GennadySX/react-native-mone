//
//  UIImage+Extentions.swift
//  Mone
//
//  Created by Gennady Sabirovsky on 13.03.2023.
//  Copyright © 2023 Facebook. All rights reserved.
//

import Foundation
import UIKit
import AVFoundation

enum FilterType: String {
    case sepia
    case blur
    case sharpen
    case grayscale
    case blackAndWhite
    case off

}
extension UIImage {

  
  func filter( filter: FilterType) -> UIImage? {
      switch filter {
        case FilterType.sepia:
           return self.filterSepia()
        case FilterType.blur:
        return self.filterBlur()
        case FilterType.sharpen:
        return self.filterPolaris()
        case FilterType.grayscale:
        return self.filterGrayScale()
        case FilterType.blackAndWhite:
        return self.filterBlackAndWhite()
        default:
        return self
      }

  }

    func filterBlackAndWhite() -> UIImage? {
        let inputImage = CIImage(image: self)
        let filter = CIFilter(name: "CIPhotoEffectNoir")
        filter?.setValue(inputImage, forKey: kCIInputImageKey)
        return self.checkIsEmpty(filter?.outputImage)
    }

    func filterGrayScale() -> UIImage? {
        let inputImage = CIImage(image: self)
        let filter = CIFilter(name: "CIPhotoEffectMono")
        filter?.setValue(inputImage, forKey: kCIInputImageKey)
        return self.checkIsEmpty(filter?.outputImage)
    }

    func filterSepia() -> UIImage? {
        let inputImage = CIImage(image: self)
        let filter = CIFilter(name: "CISepiaTone")
        filter?.setValue(inputImage, forKey: kCIInputImageKey)
        filter?.setValue(0.8, forKey: kCIInputIntensityKey)
        return self.checkIsEmpty(filter?.outputImage)
    }

    func filterContrast() -> UIImage? {
        let inputImage = CIImage(image: self)
        let filter = CIFilter(name: "CIColorControls")
        filter?.setValue(inputImage, forKey: kCIInputImageKey)
        filter?.setValue(1.5, forKey: kCIInputContrastKey)
        return self.checkIsEmpty(filter?.outputImage)
    }


    func filterPolaris() -> UIImage? {
        let inputImage = CIImage(image: self)
        let filter = CIFilter(name: "CIPhotoEffectInstant")
        filter?.setValue(inputImage, forKey: kCIInputImageKey)
        return self.checkIsEmpty(filter?.outputImage)
    }


    func filterBlur() -> UIImage? {
        guard let inputImage = CIImage(image: self) else {
            return nil
        }
        guard let filter = CIFilter(name: "CIGaussianBlur") else {
            return nil
        }
        filter.setValue(inputImage, forKey: kCIInputImageKey)
        filter.setValue(15, forKey: kCIInputRadiusKey)

        // UIKit and UIImageView .contentMode doesn't play well with
        // CIImage only, so we need to back the return UIImage with a CGImage
        let context = CIContext()
        guard let output = filter.outputImage else {
            return nil
        }

        // cropping rect because blur changed size of image

        // to clear the blurred edges, use a fromRect that is
        // the original image extent insetBy (negative) 1/2 of new extent origins
        let newExtent = inputImage.extent.insetBy(dx: -output.extent.origin.x * 0.5, dy: -output.extent.origin.y * 0.5)
        guard let final = context.createCGImage(output, from: newExtent) else {
            return nil
        }
        return UIImage(cgImage: final)

        //return self.checkIsEmpty(filter?.outputImage)
    }


  
    func checkIsEmpty(_ outputImage: CIImage?) -> UIImage {
    let context = CIContext(options: nil)
    if(outputImage != nil) {
      let cgImg = context.createCGImage(outputImage!, from: outputImage!.extent)
      let newImage = UIImage(cgImage: cgImg!)
      return newImage
    } else {
      return self
    }
  }

    //func saveImage()

}


@available(iOS 10.0, *)
extension UIImage {

    func resizeImage(size: CGSize) -> UIImage? {
        //

        let availableRect = AVFoundation.AVMakeRect(aspectRatio: self.size, insideRect: .init(origin: .zero, size: size))
        let targetSize = availableRect.size

        let format = UIGraphicsImageRendererFormat()
        format.scale = 1
        let renderer = UIGraphicsImageRenderer(size: targetSize, format: format)

        let resized = renderer.image { (context) in
            self.draw(in: CGRect(origin: .zero, size: targetSize))
        }

        return resized
    }


    func resizeImageSimple(size: CGSize) -> UIImage? {
        let renderer = UIGraphicsImageRenderer(size: size)
        let image = renderer.image { _ in
            self.draw(in: CGRect(origin: .zero, size: size))
        }
        return image
    }


    func scalePreservingAspectRatio(targetSize: CGSize) -> UIImage {
        // Determine the scale factor that preserves aspect ratio
        let widthRatio = targetSize.width / size.width
        let heightRatio = targetSize.height / size.height

        let scaleFactor = min(widthRatio, heightRatio)

        // Compute the new image size that preserves aspect ratio
        let scaledImageSize = CGSize(
                width: size.width * scaleFactor,
                height: size.height * scaleFactor
        )

        // Draw and return the resized UIImage

            let renderer = UIGraphicsImageRenderer(
                size: scaledImageSize
            )
        
        let scaledImage = renderer.image { _ in
            self.draw(in: CGRect(
                    origin: .zero,
                    size: scaledImageSize
            ))
        }

        return scaledImage
    }

}
