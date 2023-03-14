//
// Created by Gennady Sabirovsky on 13.03.2023.
// Copyright (c) 2023 Facebook. All rights reserved.
//

import Foundation
import UIKit

@available(iOS 10.0, *)
class MoneView : UIView {

    var width: CGFloat = 0
    var height: CGFloat = 0
    var filter: FilterType = .off
    var source: String = ""
    var borderRadius: CGFloat = 0

    let imageView = UIImageView()


    override init(frame: CGRect) {
        super.init(frame: frame)

    }


    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        imageView.frame = CGRect(x: 0, y: 0, width: width, height: height)


    }


    func setupImageView() {
        let size  = CGSize(width: width, height: height)
        let url = URL(string: source)
        let data = try? Data(contentsOf: url!)
        let image = UIImage(data: data!)
//       let resizedImage = image?.scalePreservingAspectRatio(targetSize: CGSize(width: width, height: height))
//       let resizedImage = image?.resizeImage(size: size )
        let resizedImage = image?.resizeImageSimple(size: size)
        imageView.image = resizedImage?.filter(filter: self.filter)
        imageView.layer.cornerRadius = borderRadius
        self.frame = CGRect(x: 0, y: 0, width: width, height: height)
        addSubview(imageView)
    }


    @objc func setStyle(_ style: NSDictionary) {
        self.borderRadius = (style["borderRadius"] ?? 0) as! CGFloat
    }

    @objc func setSource(_ source: NSDictionary) {
        let filterStr = source["filter"] as! String
        switch filterStr {
        case "SEPIA":
            self.filter = .sepia
        case "BLUR":
            self.filter = .blur
        case "SHARPEN":
            self.filter = .sharpen
        case "GRAYSCALE":
            self.filter = .grayscale
        case "INVERT":
            self.filter = .blackAndWhite
        default:
            self.filter = .off
        }


        self.source = (source["uri"] ?? "") as! String
        self.width = (source["width"] ?? 0) as! CGFloat
        self.height = (source["height"] ?? 0) as! CGFloat

        print("filter: \(self.filter), source: \(self.source), width: \(self.width), height: \(self.height)")

        if(!self.source.isEmpty) {
            setupImageView()
        }
    }


}
