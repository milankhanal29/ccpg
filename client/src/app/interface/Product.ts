export interface IProduct {
  image: any;
  productCategoryId: number;
  decodedProductImage: any;
  fullDescription: string;
  productName: string;
  productQuantity: number;
  rate: string;
  unit: string;
  productPrice: number;
  weight: string;
  discount: string;
  imageName: string;
  productImage: ArrayBuffer;
  productDescription: string;
  isAvailable: boolean;
  perUnitPrice: number;
  quantityType: string;
  availableQuantity: number;
  status: any;
  createdDate: Date;
  id: number
  brandName: string;
  brandDescription: string;
  decodedBrandImage: any;
  brandLogo: ArrayBuffer;
  quantity: number;
  productId: number

}
export interface ICart {
  calculatedTotalPrice: number;
  decodedProductImage: any;
  id:number
  productId:number;
  userId:number;
  decodedBrandImage: any;
  product:IProduct
  image:any
  quantity:number
  productPrice:number
}
